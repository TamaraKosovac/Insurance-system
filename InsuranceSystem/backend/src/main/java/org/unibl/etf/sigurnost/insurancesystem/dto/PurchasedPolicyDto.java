package org.unibl.etf.sigurnost.insurancesystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class PurchasedPolicyDto {
    private Long id;
    private String name;
    private String type;
    private double amount;
    private String description;
    private LocalDateTime purchaseDate;
}
