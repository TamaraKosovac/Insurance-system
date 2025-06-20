package org.unibl.etf.sigurnost.insurancesystem.dto;
import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;
}