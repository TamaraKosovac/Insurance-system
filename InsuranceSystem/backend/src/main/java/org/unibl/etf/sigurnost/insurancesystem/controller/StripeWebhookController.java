package org.unibl.etf.sigurnost.insurancesystem.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stripe.Stripe;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.net.Webhook;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.unibl.etf.sigurnost.insurancesystem.model.Policy;
import org.unibl.etf.sigurnost.insurancesystem.model.Purchase;
import org.unibl.etf.sigurnost.insurancesystem.model.User;
import org.unibl.etf.sigurnost.insurancesystem.repository.PolicyRepository;
import org.unibl.etf.sigurnost.insurancesystem.repository.PurchaseRepository;
import org.unibl.etf.sigurnost.insurancesystem.repository.UserRepository;
import org.unibl.etf.sigurnost.insurancesystem.service.AccessControllerService;
import org.unibl.etf.sigurnost.insurancesystem.service.EmailService;
import org.unibl.etf.sigurnost.insurancesystem.service.PdfGeneratorService;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/stripe")
@RequiredArgsConstructor
public class StripeWebhookController {

    private final UserRepository userRepository;
    private final PolicyRepository policyRepository;
    private final PurchaseRepository purchaseRepository;
    private final EmailService emailService;
    private final PdfGeneratorService pdfGeneratorService;
    private final AccessControllerService accessControllerService;
    private final jakarta.servlet.http.HttpServletRequest request;


    @Value("${stripe.webhook-secret}")
    private String webhookSecret;

    @Value("${stripe.secret-key}")
    private String stripeApiKey;

    @PostConstruct
    public void init() {
        Stripe.apiKey = stripeApiKey;
    }

    @PostMapping("/webhook")
    public ResponseEntity<String> handleStripeWebhook(
            @RequestBody String payload,
            @RequestHeader("Stripe-Signature") String sigHeader) {

        try {
            Event event = Webhook.constructEvent(payload, sigHeader, webhookSecret);

            if (!"checkout.session.completed".equals(event.getType())) {
                return ResponseEntity.ok("Ignored event type: " + event.getType());
            }

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(payload);
            JsonNode session = root.path("data").path("object");

            String extractedEmail = session.path("customer_email").asText();
            String policyIdStr = session.path("client_reference_id").asText();
            long amount = session.path("amount_total").asLong();
            String authHeader = request.getHeader("Authorization");
            String token = authHeader != null && authHeader.startsWith("Bearer ") ? authHeader : null;

            if (amount > 1000000) {
                accessControllerService.logSuspiciousEvent(
                        extractedEmail,
                        "LARGE_PAYMENT: $" + (amount / 100.0),
                        true,
                        request,
                        token
                );
            } else {
                accessControllerService.logSuspiciousEvent(
                        extractedEmail,
                        "Payment: $" + (amount / 100.0),
                        false,
                        request,
                        token
                );
            }

            String transactionId = session.path("payment_intent").asText();


            Long policyId = Long.parseLong(policyIdStr);

            if (purchaseRepository.findByTransactionId(transactionId).isPresent()) {
                return ResponseEntity.ok("Transaction already processed.");
            }

            User user = userRepository.findByEmail(extractedEmail)
                    .orElseThrow(() -> new RuntimeException("User not found for email: " + extractedEmail));

            Policy policy = policyRepository.findById(policyId)
                    .orElseThrow(() -> new RuntimeException("Policy not found for id: " + policyId));

            Purchase purchase = Purchase.builder()
                    .policyId(policyId)
                    .userId(user.getId())
                    .amount(amount / 100.0)
                    .purchaseDate(LocalDateTime.now())
                    .transactionId(transactionId)
                    .build();

            purchaseRepository.save(purchase);

            byte[] pdf = pdfGeneratorService.generatePolicyPdf(policy, user, transactionId);

            emailService.sendPolicyPdf(extractedEmail, pdf);

            return ResponseEntity.ok("Webhook handled and email sent");

        } catch (SignatureVerificationException e) {
            System.out.println("❌ Invalid Stripe signature");
            return ResponseEntity.status(400).body("Invalid signature");
        } catch (Exception ex) {
            System.out.println("❌ Error during webhook handling");
            ex.printStackTrace();
            return ResponseEntity.status(500).body("Error handling webhook");
        }
    }
}
