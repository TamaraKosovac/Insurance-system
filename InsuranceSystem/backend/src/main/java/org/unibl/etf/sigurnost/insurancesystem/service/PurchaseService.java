package org.unibl.etf.sigurnost.insurancesystem.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.unibl.etf.sigurnost.insurancesystem.model.Policy;
import org.unibl.etf.sigurnost.insurancesystem.model.Purchase;
import org.unibl.etf.sigurnost.insurancesystem.model.User;
import org.unibl.etf.sigurnost.insurancesystem.repository.PolicyRepository;
import org.unibl.etf.sigurnost.insurancesystem.repository.PurchaseRepository;
import org.unibl.etf.sigurnost.insurancesystem.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PurchaseService {

    private final PurchaseRepository purchaseRepository;
    private final PolicyRepository policyRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final PdfGeneratorService pdfGeneratorService;

    public void purchasePolicy(Long userId, Long policyId) {

        Policy policy = policyRepository.findById(policyId)
                .orElseThrow(() -> new RuntimeException("Policy not found"));

        String transactionId = UUID.randomUUID().toString();

        Purchase purchase = Purchase.builder()
                .policyId(policyId)
                .userId(userId)
                .amount(policy.getAmount())
                .purchaseDate(LocalDateTime.now())
                .transactionId(transactionId)
                .build();

        purchaseRepository.save(purchase);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        byte[] pdf = pdfGeneratorService.generatePolicyPdf(policy, user, transactionId);

        emailService.sendPolicyPdf(user.getEmail(), pdf);
    }
}
