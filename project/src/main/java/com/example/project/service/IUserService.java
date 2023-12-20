package com.example.project.service;

import com.example.project.dto.request.UserCreateRequestDTO;
import com.example.project.dto.response.UserDTO;
import com.example.project.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;

public interface IUserService extends IGeneralService<User>, UserDetailsService {
    Optional<User> findByEmail(String email);

    UserDTO createUser(UserCreateRequestDTO userCreateDTO) throws Exception;
    User getUserByID(Long userID) throws Exception;
    Page<UserDTO> getAllUsers(String keysearch, Pageable pageable);

    UserDTO updateUser(Long userID, UserDTO userDTO);

    void deleteUser(Long id);

    Page<User> findNameOrEmail (String lastName, Pageable pageable);

    boolean existsByEmail(String email);

    Page<User> findAll(Pageable pageable);
}
