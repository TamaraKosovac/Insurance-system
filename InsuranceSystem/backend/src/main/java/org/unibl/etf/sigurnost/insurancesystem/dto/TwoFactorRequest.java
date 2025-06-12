package org.unibl.etf.sigurnost.insurancesystem.dto;
import lombok.Data;

@Data
public class TwoFactorRequest {
    private String username;
    private String code;
}