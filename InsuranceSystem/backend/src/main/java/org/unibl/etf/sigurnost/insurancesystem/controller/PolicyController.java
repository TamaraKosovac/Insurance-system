package org.unibl.etf.sigurnost.insurancesystem.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.unibl.etf.sigurnost.insurancesystem.dto.PurchasedPolicyDto;
import org.unibl.etf.sigurnost.insurancesystem.model.Policy;
import org.unibl.etf.sigurnost.insurancesystem.model.Purchase;
import org.unibl.etf.sigurnost.insurancesystem.model.User;
import org.unibl.etf.sigurnost.insurancesystem.repository.PolicyRepository;
import org.unibl.etf.sigurnost.insurancesystem.repository.PurchaseRepository;
import org.unibl.etf.sigurnost.insurancesystem.service.PolicyService;

import java.util.List;

@RestController
@RequestMapping("/api/policies")
@CrossOrigin(origins = {"https://localhost:4200", "https://localhost:4300"})
@RequiredArgsConstructor
public class PolicyController {

    private final PolicyService policyService;
    private final PolicyRepository policyRepository;
    private final PurchaseRepository purchaseRepository;

    @GetMapping
    @PreAuthorize("hasAnyRole('CLIENT', 'ADMIN', 'EMPLOYEE')")
    public List<Policy> getAllPolicies() {
        return policyService.findAll();
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public ResponseEntity<Policy> createPolicy(@RequestBody Policy policy) {
        System.out.println("Policy received: " + policy);
        return ResponseEntity.ok(policyService.create(policy));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public ResponseEntity<Void> deletePolicy(@PathVariable Long id) {
        policyService.delete(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public ResponseEntity<Policy> updatePolicy(@PathVariable Long id, @RequestBody Policy policy) {
        return ResponseEntity.ok(policyService.update(id, policy));
    }

    @GetMapping("/purchased")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE', 'CLIENT')")
    public ResponseEntity<List<PurchasedPolicyDto>> getPurchasedPolicies() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = user.getId();

        List<Purchase> purchases = purchaseRepository.findByUserId(userId);

        List<PurchasedPolicyDto> result = purchases.stream()
                .map(purchase -> {
                    Policy policy = policyRepository.findById(purchase.getPolicyId())
                            .orElseThrow(() -> new RuntimeException("Policy not found for ID: " + purchase.getPolicyId()));
                    return new PurchasedPolicyDto(
                            policy.getId(),
                            policy.getName(),
                            policy.getType().name(),
                            policy.getAmount(),
                            policy.getDescription(),
                            purchase.getPurchaseDate()
                    );
                })
                .toList();

        return ResponseEntity.ok(result);
    }
}
