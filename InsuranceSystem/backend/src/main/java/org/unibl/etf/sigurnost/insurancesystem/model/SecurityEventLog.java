package org.unibl.etf.sigurnost.insurancesystem.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "security_event_logs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SecurityEventLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String ipAddress;
    private String action;
    private LocalDateTime timestamp;
    private boolean suspicious;
}
