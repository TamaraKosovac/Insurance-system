package org.unibl.etf.sigurnost.insurancesystem.controller;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.unibl.etf.sigurnost.insurancesystem.dto.JwtResponse;
import org.unibl.etf.sigurnost.insurancesystem.dto.LoginRequest;
import org.unibl.etf.sigurnost.insurancesystem.dto.RegisterRequest;
import org.unibl.etf.sigurnost.insurancesystem.dto.TwoFactorRequest;
import org.unibl.etf.sigurnost.insurancesystem.service.AuthService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "https://localhost:4200")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<String> register(
            @RequestParam String firstname,
            @RequestParam String lastname,
            @RequestParam String username,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam(required = false) MultipartFile profileImage
    ) {
        String imageUrl = null;

        if (profileImage != null && !profileImage.isEmpty()) {
            try {
                String fileName = UUID.randomUUID() + "-" + profileImage.getOriginalFilename();
                Path path = Paths.get("uploads", fileName);
                Files.createDirectories(path.getParent());
                Files.copy(profileImage.getInputStream(), path);
                imageUrl = "/uploads/" + fileName;
            } catch (IOException e) {
                return ResponseEntity.internalServerError().body("Error while saving image.");
            }
        }

        try {
            RegisterRequest request = new RegisterRequest();
            request.setFirstname(firstname);
            request.setLastname(lastname);
            request.setUsername(username);
            request.setEmail(email);
            request.setPassword(password);

            authService.register(request, imageUrl);
            return ResponseEntity.ok("Registration successful.");
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }


    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest request) {
        authService.login(request.getUsername(), request.getPassword());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/verify")
    public ResponseEntity<JwtResponse> verify(@RequestBody TwoFactorRequest request) {
        String token = authService.verifyCode(request.getUsername(), request.getCode());
        return ResponseEntity.ok(new JwtResponse(token));
    }

    @PostMapping("/resend")
    public ResponseEntity<String> resend(@RequestBody TwoFactorRequest request) {
        authService.resendVerificationCode(request.getUsername());
        return ResponseEntity.ok("Verification code resent.");
    }

}