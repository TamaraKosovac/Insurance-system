package org.unibl.etf.sigurnost.insurancesystem.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.unibl.etf.sigurnost.insurancesystem.service.JwtService;
import org.unibl.etf.sigurnost.insurancesystem.service.PurchaseService;

import java.util.Map;

@RestController
@RequestMapping("/api/purchase")
@RequiredArgsConstructor
public class PurchaseController {

    private final PurchaseService purchaseService;
    private final JwtService jwtService;

    @PostMapping
    public ResponseEntity<?> buyPolicy(@RequestBody Map<String, Long> body, HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        String username = jwtService.extractUsername(token);
        Long userId = jwtService.extractUserId(token);

        Long policyId = body.get("policyId");

        purchaseService.purchasePolicy(userId, policyId);

        return ResponseEntity.ok(Map.of("message", "Policy purchased successfully"));
    }
}
