package org.unibl.etf.sigurnost.insurancesystem.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.unibl.etf.sigurnost.insurancesystem.dto.RegisterRequest;
import org.unibl.etf.sigurnost.insurancesystem.model.Role;
import org.unibl.etf.sigurnost.insurancesystem.model.User;
import org.unibl.etf.sigurnost.insurancesystem.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final JwtService jwtService;

    public void register(RegisterRequest request, String imageUrl) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists.");
        }

        if (!isPasswordStrong(request.getPassword())) {
            throw new RuntimeException("Password must be at least 8 characters long, contain one uppercase letter, one number, and one special character.");
        }

        User user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .enabled(false)
                .role(Role.CLIENT)
                .profileImageUrl(imageUrl)
                .build();

        userRepository.save(user);
    }

    private boolean isPasswordStrong(String password) {
        return password.matches("^(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$");
    }

    public void login(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        String code = String.valueOf(new Random().nextInt(900000) + 100000);
        user.setVerificationCode(code);
        user.setCodeExpiry(LocalDateTime.now().plusMinutes(10));
        userRepository.save(user);

        emailService.sendVerificationCode(user.getEmail(), code);
    }

    public String verifyCode(String username, String code) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!code.equals(user.getVerificationCode()))
            throw new RuntimeException("Invalid verification code");

        if (user.getCodeExpiry().isBefore(LocalDateTime.now()))
            throw new RuntimeException("Verification code expired");

        user.setEnabled(true);
        user.setVerificationCode(null);
        user.setCodeExpiry(null);
        userRepository.save(user);

        return jwtService.generateToken(user);
    }

    public void resendVerificationCode(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String code = String.valueOf(new Random().nextInt(900000) + 100000);
        user.setVerificationCode(code);
        user.setCodeExpiry(LocalDateTime.now().plusMinutes(10));
        userRepository.save(user);

        emailService.sendVerificationCode(user.getEmail(), code);
    }
}
