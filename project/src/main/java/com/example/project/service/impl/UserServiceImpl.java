package com.example.project.service.impl;

import com.example.project.dto.request.UserCreateRequestDTO;
import com.example.project.dto.response.UserDTO;
import com.example.project.exception.ErrorException;
import com.example.project.exception.ResponseMessage;
import com.example.project.model.Role;
import com.example.project.model.User;
import com.example.project.repository.IRoleRepository;
import com.example.project.repository.IUserRepository;
import com.example.project.model.UserPrinciple;
import com.example.project.service.IUserService;
import com.example.project.service.JwtService;
import com.example.project.utlis.validator.UserValidator;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;

import static java.net.HttpURLConnection.*;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {

    private final IUserRepository userRepository;

    private final IRoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;
    private final ModelMapper mapper = new ModelMapper();
    private final MessageSource messageSource;
    private final UserValidator accountValidator;
    @Override
    public Iterable<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }
    private static final Logger logger = LoggerFactory.getLogger(JwtService.class.getName());


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
    public UserDTO createUser(UserCreateRequestDTO userCreateDTO) {
        accountValidator.validateCreateRequest(userCreateDTO);
        Role role = roleRepository.findById(userCreateDTO.getRoleId())
                .orElseThrow(() -> new ErrorException(messageSource.getMessage("ERROR_ROLE_NOT_FOUND", null, LocaleContextHolder.getLocale()), HTTP_NOT_FOUND));
        var newUser = User.builder()
                .email(userCreateDTO.getEmail())
                .firstName(userCreateDTO.getFirstName())
                .lastName(userCreateDTO.getLastName())
                .address(userCreateDTO.getAddress())
                .password(passwordEncoder.encode(userCreateDTO.getPassword()))
                .phone(userCreateDTO.getPhone())
                .role(role)
                .build();

        try {
            userRepository.save(newUser);
        }
        catch (Exception e) {
            throw new ErrorException(messageSource.getMessage("USER_ID_INVALID", null, LocaleContextHolder.getLocale()), HTTP_INTERNAL_ERROR);
        }
        return UserDTO.builder()
                .id(newUser.getId())
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
                .orElseThrow(() -> new ErrorException(messageSource.getMessage("USER_ID_INVALID", null, LocaleContextHolder.getLocale()),HTTP_NOT_FOUND));
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
                oldUser.setFirstName(userDTO.getFirstName());
                oldUser.setLastName(userDTO.getLastName());
                oldUser.setAddress(userDTO.getAddress());
                oldUser.setPhone(userDTO.getPhone());
                oldUser.setUpdatedAt(LocalDateTime.now());
                Optional<Role> roleUpdate = roleRepository.findById(userDTO.getRoleId());
                if(roleUpdate.isPresent()){
                    oldUser.setRole(roleUpdate.get());
                }else{
                    new ResponseMessage(messageSource.getMessage("USER_UPDATE_SUCCESS",null, LocaleContextHolder.getLocale()),HTTP_OK);
                }
                return mapper.map(userRepository.save(oldUser),UserDTO.class);

        }else {
            throw new ErrorException(messageSource.getMessage("USER_NOT_FOUND", null, LocaleContextHolder.getLocale()), HTTP_NOT_FOUND);
        }

    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(
                        () -> new ErrorException(messageSource.getMessage("USER_NOT_FOUND", null, LocaleContextHolder.getLocale()),HTTP_NOT_FOUND));
        existingUser.setDeleteFlag(true);
        userRepository.save(existingUser);
    }



}
