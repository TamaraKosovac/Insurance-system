package org.unibl.etf.sigurnost.insurancesystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Long id;
    private String username;
    private String email;
    private String role;
    private boolean enabled;
    private String firstname;
    private String lastname;
    private String profileImageUrl;
    private String password;

    public UserDto(Long id, String username, String email, String role, boolean enabled,
                   String firstname, String lastname, String profileImageUrl) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.role = role;
        this.enabled = enabled;
        this.firstname = firstname;
        this.lastname = lastname;
        this.profileImageUrl = profileImageUrl;
        this.password = null;
    }
}
