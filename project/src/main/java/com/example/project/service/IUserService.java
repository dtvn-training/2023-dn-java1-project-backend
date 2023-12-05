package com.example.project.service;

import com.example.project.dto.UserDTO;
import com.example.project.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;

public interface IUserService extends IGeneralService<User>, UserDetailsService {
    Optional<User> findByEmail(String email);

    UserDTO createUser(UserDTO userDTO) throws Exception;
    User getUserByID(Long userID) throws Exception;
    Page<UserDTO> getAllUsers(String keysearch, Pageable pageable);

    UserDTO updateUser(Long userID, UserDTO userDTO);

    void deleteUser(Long id);
}
