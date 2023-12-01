package com.example.project.service.impl;

import com.example.project.constants.ErrorMessage;
import com.example.project.dto.UserCreateDTO;
import com.example.project.dto.UserDTO;
import com.example.project.exception.ErrorException;
import com.example.project.model.Role;
import com.example.project.model.User;
import com.example.project.payload.response.UserResponse;
import com.example.project.repository.RoleRepository;
import com.example.project.repository.UserRepository;
import com.example.project.model.UserPrinciple;
import com.example.project.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

import static com.example.project.constants.ErrorConstants.ERROR_ROLE_NOT_FOUND;
import static com.example.project.constants.ErrorMessage.USER_ID_INVALID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;
    private final ModelMapper mapper = new ModelMapper();
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
    public UserDTO createUser(UserCreateDTO userCreateDTO) {
        Role role = roleRepository.findById(userCreateDTO.getRoleId())
                .orElseThrow(() -> new ErrorException(ERROR_ROLE_NOT_FOUND, ErrorMessage.RESOURCE_NOT_FOUND_CODE));
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
                .orElseThrow(() -> new ErrorException(USER_ID_INVALID, ErrorMessage.RESOURCE_NOT_FOUND_CODE));
    }

    @Override
    public Page<UserDTO> getAllUsers(String keySearch, Pageable pageable) {
        if(keySearch == null || keySearch.isEmpty()){
            Page<User> allUser = userRepository.findAll(pageable);
            return allUser.map(user -> mapper.map(user, UserDTO.class));
        }
        else {
            Page<User> allUser = userRepository.findNameOrEmail(keySearch,pageable);
            return allUser.map(user -> mapper.map(user, UserDTO.class));
        }
    }

    @Override
    public UserDTO updateUser(Long userID, UserDTO userDTO) {
        Optional<User> optionalOldUser = userRepository.findById(userID);
        if(optionalOldUser.isPresent()){
            User oldUser  =  optionalOldUser.get();
            oldUser.setEmail(userDTO.getEmail());
            oldUser.setFirstName(userDTO.getFirstName());
            oldUser.setLastName(userDTO.getLastName());
            oldUser.setAddress(userDTO.getAddress());
            oldUser.setPhone(userDTO.getPhone());
            //find role id by role name
            Optional<Role> roleUpdate = roleRepository.findById(userDTO.getRoleId());
            if(roleUpdate.isPresent()){
                oldUser.setRole(roleUpdate.get());
            }else{
                throw new ErrorException(ERROR_ROLE_NOT_FOUND, ErrorMessage.RESOURCE_NOT_FOUND_CODE);
            }
            return mapper.map(userRepository.save(oldUser),UserDTO.class);
        }else {
            throw new ErrorException(ErrorMessage.USER_NOT_FOUND,  ErrorMessage.RESOURCE_NOT_FOUND_CODE);
        }

    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(
                        () -> new RuntimeException(ErrorMessage.USER_NOT_FOUND));
        existingUser.setDeleteFlag(true);
        userRepository.save(existingUser);
    }



}
