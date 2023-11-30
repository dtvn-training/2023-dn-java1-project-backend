package com.example.project.service.impl;

import com.example.project.dto.UserCreateDTO;
import com.example.project.dto.UserDTO;
import com.example.project.exception.DataNotFoundException;
import com.example.project.model.Role;
import com.example.project.model.User;
import com.example.project.payload.response.UserResponse;
import com.example.project.repository.RoleRepository;
import com.example.project.repository.UserRepository;
import com.example.project.model.UserPrinciple;
import com.example.project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepository.findByEmail(username);
        if (!userOptional.isPresent()) {
            throw new UsernameNotFoundException(username);
        }
        return UserPrinciple.build(userOptional.get());
    }


    @Override
    public UserDTO createUser(UserCreateDTO userCreateDTO) throws DataNotFoundException {
        Role role = roleRepository.findById(userCreateDTO.getRoleId())
                .orElseThrow(() -> new DataNotFoundException("Role not found "));
        System.out.println(role);
        User newUser = User.builder()
                .email(userCreateDTO.getEmail())
                .firstName(userCreateDTO.getFirstName())
                .lastName(userCreateDTO.getLastName())
                .password(passwordEncoder.encode(userCreateDTO.getPassword()))
                .address(userCreateDTO.getAddress())
                .phone(userCreateDTO.getPhone())
                .role(role)
                .build();
        userRepository.save(newUser);
        return UserDTO.builder()
                .firstName(newUser.getFirstName())
                .lastName(newUser.getLastName())
                .email(newUser.getEmail())
                .roleId(newUser.getRole().getId())
                .address(newUser.getAddress())
                .phone(newUser.getPhone())
                .build();
    }

    @Override
    public User getUserByID(Long userID) throws Exception {
        return userRepository.findById(userID)
                .orElseThrow(() -> new DataNotFoundException("cannot find user not found id: " + userID));
    }

    @Override
    public Page<UserResponse> getAllUsers(PageRequest pageRequest) {
        // get number of user  for page and limit
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
        exitstingUser.setRole(exitstingRole);
        return userRepository.save(exitstingUser);

    }

    @Override
    public void deleteUser(Long id) throws DataNotFoundException {
        User existingUser = userRepository.findById(id)
                .orElseThrow(
                        () -> new DataNotFoundException
                                ("Cannot delete order with id: " + id));
        existingUser.setDeleteFlag(true);
        userRepository.save(existingUser);
    }



}
