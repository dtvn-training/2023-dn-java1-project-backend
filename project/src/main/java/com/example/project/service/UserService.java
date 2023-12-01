package com.example.project.service;

import com.example.project.dto.UserCreateDTO;
import com.example.project.dto.UserDTO;
import com.example.project.model.User;
import com.example.project.payload.response.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;

public interface UserService extends GeneralService<User>, UserDetailsService {
    Optional<User> findByEmail(String email);

    UserDTO createUser(UserCreateDTO userCreateDTO) throws Exception;
    User getUserByID(Long userID) throws Exception;
    Page<UserDTO> getAllUsers(String keysearch, Pageable pageable);

    UserDTO updateUser(Long userID, UserDTO userDTO);

    void deleteUser(Long id);
}
