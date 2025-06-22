package org.unibl.etf.sigurnost.insurancesystem.controller;

import com.stripe.exception.StripeException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.unibl.etf.sigurnost.insurancesystem.model.Policy;
import org.unibl.etf.sigurnost.insurancesystem.model.User;
import org.unibl.etf.sigurnost.insurancesystem.repository.PolicyRepository;
import org.unibl.etf.sigurnost.insurancesystem.service.StripeService;

import java.util.Map;

@RestController
@RequestMapping("/api/stripe")
@RequiredArgsConstructor
public class StripeController {

    private final StripeService stripeService;
    private final PolicyRepository policyRepository;

    @PostMapping("/checkout")
    public ResponseEntity<Map<String, String>> createCheckoutSession(@RequestBody Map<String, Object> body) throws StripeException {
        Long policyId = Long.valueOf(body.get("policyId").toString());

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = user.getEmail();

        Policy policy = policyRepository.findById(policyId)
                .orElseThrow(() -> new RuntimeException("Policy not found"));

        String url = stripeService.createCheckoutSession(
                policy,
                "https://localhost:4300/client/policies",
                "https://localhost:4300/client/policies",
                email
        );

        return ResponseEntity.ok(Map.of("url", url));
    }
}
