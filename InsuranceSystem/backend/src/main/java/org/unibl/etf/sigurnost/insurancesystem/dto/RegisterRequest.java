package org.unibl.etf.sigurnost.insurancesystem.dto;
import lombok.Data;
import org.unibl.etf.sigurnost.insurancesystem.model.Role;

@Data
public class RegisterRequest {
    private String firstname;
    private String lastname;
    private String username;
    private String email;
    private String password;
    private Role role;
}