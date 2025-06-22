package org.unibl.etf.sigurnost.insurancesystem.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.unibl.etf.sigurnost.insurancesystem.dto.JwtResponse;
import org.unibl.etf.sigurnost.insurancesystem.dto.LoginRequest;
import org.unibl.etf.sigurnost.insurancesystem.dto.RegisterRequest;
import org.unibl.etf.sigurnost.insurancesystem.dto.TwoFactorRequest;
import org.unibl.etf.sigurnost.insurancesystem.repository.UserRepository;
import org.unibl.etf.sigurnost.insurancesystem.service.AccessControllerService;
import org.unibl.etf.sigurnost.insurancesystem.service.AuthService;

import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = {"https://localhost:4200", "https://localhost:4300"})
public class AuthController {

    private final AuthService authService;
    private final UserRepository userRepository;
    private final AccessControllerService accessControllerService;

    private final Map<String, Integer> failedAttempts = new ConcurrentHashMap<>();

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
    public ResponseEntity<String> login(@RequestBody LoginRequest request, HttpServletRequest httpRequest) {
        String username = request.getUsername();
        try {
            authService.login(username, request.getPassword());
            failedAttempts.remove(username);
            return ResponseEntity.ok().build();
        } catch (RuntimeException ex) {
            int attempts = failedAttempts.getOrDefault(username, 0) + 1;
            failedAttempts.put(username, attempts);

            if (attempts >= 5) {
                accessControllerService.logSuspiciousEvent(
                        username,
                        "Multiple failed login attempts: " + attempts,
                        true,
                        httpRequest
                );
                failedAttempts.remove(username);
            }

            throw ex;
        }
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

    @GetMapping("/role/{username}")
    public ResponseEntity<String> getUserRole(@PathVariable String username) {
        return userRepository.findByUsername(username)
                .map(user -> ResponseEntity.ok(user.getRole().name()))
                .orElse(ResponseEntity.notFound().build());
    }
}
