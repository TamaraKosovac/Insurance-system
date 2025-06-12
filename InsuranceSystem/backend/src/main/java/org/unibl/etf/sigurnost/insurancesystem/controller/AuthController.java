package org.unibl.etf.sigurnost.insurancesystem.controller;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.unibl.etf.sigurnost.insurancesystem.dto.JwtResponse;
import org.unibl.etf.sigurnost.insurancesystem.dto.LoginRequest;
import org.unibl.etf.sigurnost.insurancesystem.dto.RegisterRequest;
import org.unibl.etf.sigurnost.insurancesystem.dto.TwoFactorRequest;
import org.unibl.etf.sigurnost.insurancesystem.service.AuthService;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
        authService.register(request);
        return ResponseEntity.ok("✅ Registration successful.");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest request) {
        authService.login(request.getUsername(), request.getPassword());
        return ResponseEntity.ok("✅ Login successful. Check your email for verification code.");
    }

    @PostMapping("/verify")
    public ResponseEntity<JwtResponse> verify(@RequestBody TwoFactorRequest request) {
        String token = authService.verifyCode(request.getUsername(), request.getCode());
        return ResponseEntity.ok(new JwtResponse(token));
    }
}