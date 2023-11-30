package com.example.project.service.impl;

import com.example.project.dto.UserDTO;
import com.example.project.exception.DataNotFoundException;
import com.example.project.model.Role;
import com.example.project.model.User;
import com.example.project.repository.RoleRepository;
import com.example.project.repository.UserRepository;
import com.example.project.model.UserPrinciple;
import com.example.project.responses.UserResponse;
import com.example.project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Iterable<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public User save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public void remove(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public  Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User createUser(UserDTO userDTO) throws DataNotFoundException {
        User newUser = User.builder()
                .email(userDTO.getEmail())
                .firstName(userDTO.getFirstName())
                .lastName(userDTO.getLastName())
                .address(userDTO.getAddress())
                .phone(userDTO.getPhone())
                .password(userDTO.getPassword())
                .build();
        Role role = roleRepository.findById(userDTO.getRoleId())
                .orElseThrow(() -> new DataNotFoundException("Role not found "));
        newUser.setRole(role);
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        return userRepository.save(newUser);
    }

    @Override
    public User getUserByID(Long userID) throws Exception {
        return userRepository.findById(userID)
                .orElseThrow(() -> new DataNotFoundException("cannot find user not found id: " + userID));
    }

    @Override
    public Page<UserResponse> getAllUser(PageRequest pageRequest) {
        // get number of product for page and limit
        return userRepository.findAll(pageRequest).map(UserResponse:: mapUser);
    }

    @Override
    public User updateUser(Long userID, UserDTO userDTO) throws Exception {
        User exitstingUser = getUserByID(userID);
        Role exitstingRole = roleRepository.findById(userDTO.getRoleId())
                .orElseThrow(() -> new DataNotFoundException("cannot find role not found id: " + userDTO.getRoleId()));
        exitstingUser.setEmail(userDTO.getEmail());
        exitstingUser.setFirstName(userDTO.getFirstName());
        exitstingUser.setLastName(userDTO.getLastName());
        exitstingUser.setAddress(userDTO.getAddress());
        exitstingUser.setPhone(userDTO.getPhone());
        exitstingUser.setPassword(userDTO.getPassword());
        exitstingUser.setRole(exitstingRole);
        return userRepository.save(exitstingUser);
    }

    @Override
    public void deleteUser(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        optionalUser.ifPresent(userRepository::delete);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepository.findByEmail(username);
        if (!userOptional.isPresent()) {
            throw new UsernameNotFoundException(username);
        }
        return UserPrinciple.build(userOptional.get());
    }

}
