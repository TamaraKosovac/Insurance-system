package org.unibl.etf.sigurnost.insurancesystem.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.unibl.etf.sigurnost.insurancesystem.model.Policy;
import org.unibl.etf.sigurnost.insurancesystem.service.PolicyService;

import java.util.List;

@RestController
@RequestMapping("/api/policies")
@CrossOrigin(origins = "https://localhost:4200")
@RequiredArgsConstructor
public class PolicyController {

    private final PolicyService policyService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
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
}
