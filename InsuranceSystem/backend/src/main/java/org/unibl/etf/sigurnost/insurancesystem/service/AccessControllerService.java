package org.unibl.etf.sigurnost.insurancesystem.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.unibl.etf.sigurnost.insurancesystem.model.RevokedToken;
import org.unibl.etf.sigurnost.insurancesystem.model.SecurityEventLog;
import org.unibl.etf.sigurnost.insurancesystem.repository.RevokedTokenRepository;
import org.unibl.etf.sigurnost.insurancesystem.repository.SecurityEventLogRepository;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AccessControllerService {

    private final SecurityEventLogRepository logRepository;
    private final RevokedTokenRepository revokedTokenRepository;

    public void logSuspiciousEvent(String username, String action, boolean suspicious, HttpServletRequest request, String token) {
        String ip = request.getRemoteAddr();

        SecurityEventLog log = SecurityEventLog.builder()
                .username(username)
                .action(action)
                .ipAddress(ip)
                .timestamp(LocalDateTime.now())
                .suspicious(suspicious)
                .build();
        logRepository.save(log);

        String logLine = String.format("[%s] user=%s ip=%s action=%s suspicious=%s%n",
                LocalDateTime.now(), username, ip, action, suspicious);
        try (FileWriter writer = new FileWriter("siem_log.txt", true)) {
            writer.write(logLine);
        } catch (IOException e) {
            System.err.println("Greška prilikom pisanja u SIEM fajl: " + e.getMessage());
        }

        if (suspicious && token != null && !token.isBlank()) {
            RevokedToken revoked = RevokedToken.builder()
                    .token(token.replace("Bearer ", ""))
                    .revokedAt(LocalDateTime.now())
                    .build();
            revokedTokenRepository.save(revoked);
            System.out.println("❌ Token je poništen zbog sumnjive aktivnosti.");
        }
    }

    public void logSuspiciousEvent(String username, String action, boolean suspicious, HttpServletRequest request) {
        logSuspiciousEvent(username, action, suspicious, request, null);
    }
}
