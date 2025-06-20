package org.unibl.etf.sigurnost.insurancesystem.service;

import org.unibl.etf.sigurnost.insurancesystem.dto.RegisterRequest;
import org.unibl.etf.sigurnost.insurancesystem.dto.UserDto;

import java.util.List;

public interface UserService {
    List<UserDto> getAllUsers();

    UserDto createUserFromAdmin(RegisterRequest request);
    UserDto updateUser(Long id, UserDto userDto);
    void deleteUser(Long id);
}
