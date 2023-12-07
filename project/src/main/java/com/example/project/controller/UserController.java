package com.example.project.controller;

import com.example.project.dto.request.UserCreateRequestDTO;
import com.example.project.dto.response.UserDTO;
import com.example.project.exception.ResponseMessage;
import com.example.project.model.Role;
import com.example.project.model.User;
import com.example.project.dto.response.UserCreateResponse;
import com.example.project.dto.response.UserResponse;
import com.example.project.repository.IRoleRepository;
import com.example.project.repository.IUserRepository;
import com.example.project.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static java.net.HttpURLConnection.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/users")
public class UserController {
    private final IUserService userService;
    private  final IRoleRepository iRoleRepository;
    private final IUserRepository iUserRepository;
    private final MessageSource messageSource;
    // Get list user
    @GetMapping("") // http://localhost:3000/api/users?page=1&limit=5
    public ResponseEntity<ResponseMessage<Page<UserDTO>>> getUsers(@RequestParam(value = "keySearch", required = false) String keySearch, @RequestParam("page") int page, @RequestParam("limit") int limit) {
        Pageable pageable = PageRequest.of(page, limit,Sort.by("createdAt").ascending());
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseMessage<Page<UserDTO>>( messageSource.getMessage("USER_GET_ALL_SUCCESS",
                        null,
                        LocaleContextHolder.getLocale()),HTTP_OK,
                        userService.getAllUsers(keySearch, pageable)));
    }

    // create user
    @PostMapping("")
    public ResponseEntity<ResponseMessage<UserDTO>>  createUser(@RequestBody UserCreateRequestDTO request) throws Exception {
        if(iUserRepository.existsByEmail(request.getEmail())){
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseMessage(messageSource.getMessage("ERROR_EMAIL_ALREADY_EXISTS",
                            null,
                            LocaleContextHolder.getLocale()), HTTP_NOT_FOUND));
        }
        UserDTO addedUser = userService.createUser(request);
        if (addedUser != null) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseMessage(messageSource.getMessage("ACCOUNT_REGISTER_SUCCESS",
                            null,
                            LocaleContextHolder.getLocale()),HTTP_OK, addedUser));
        } else {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseMessage(messageSource.getMessage("ACCOUNT_REGISTER_FAILED",
                            null,
                            LocaleContextHolder.getLocale()), HTTP_SERVER_ERROR));
        }
    }
    // Get single user
    @GetMapping("/{id}")
    public ResponseEntity<?> getUser(@PathVariable Long id) {
        try {
            Optional<User> getUser = iUserRepository.findById(id);
            //Check if account has been deleted
            if(getUser.get().isDeleteFlag())
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ResponseMessage<>(messageSource.getMessage("USER_IS_DELETED",
                                null,
                                LocaleContextHolder.getLocale()) , HTTP_OK));
            User user = userService.getUserByID(id);
            return ResponseEntity.ok(UserResponse.mapUser(user));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }
    //Update user
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@Valid @PathVariable Long id, @Valid @RequestBody UserDTO request){
        Optional<User> update = iUserRepository.findById(id);
        UserDTO userUpdated = userService.updateUser(id, request);
        System.out.println(update.get().getUpdatedAt());
        System.out.println();
        if (userUpdated != null) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseMessage(messageSource.getMessage("USER_UPDATE_SUCCESS",
                            null,
                            LocaleContextHolder.getLocale()),HTTP_OK,userUpdated));
        } else {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseMessage(messageSource.getMessage("USER_NOT_FOUND",
                            null,
                            LocaleContextHolder.getLocale()), HTTP_NOT_FOUND));
        }
    }
    // Delete User
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseMessage<UserDTO>> deleteUser(@PathVariable Long id) {
        try {
            Optional<User> accountDelete = iUserRepository.findById(id);
            //Check if account has been deleted
            if(accountDelete.get().isDeleteFlag())
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ResponseMessage<>(messageSource.getMessage("USER_IS_DELETED",
                                null,
                                LocaleContextHolder.getLocale()) , HTTP_OK));
            //delete account
            userService.deleteUser(id);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseMessage(messageSource.getMessage("USER_DELETE_SUCCESS",
                            null,
                            LocaleContextHolder.getLocale()),HTTP_OK, messageSource.getMessage("ACCOUNT_SUCCESS_CODE",
                            null,
                            LocaleContextHolder.getLocale())));
        }  catch (NumberFormatException e){
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseMessage(messageSource.getMessage("USER_ID_INVALID",
                            null,
                            LocaleContextHolder.getLocale()), HTTP_BAD_REQUEST));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseMessage(messageSource.getMessage("ACCOUNT_DELETE_FAILD",
                            null,
                            LocaleContextHolder.getLocale()), HTTP_OK,messageSource.getMessage("ACCOUNT_DELETE_FAILD",
                            null,
                            LocaleContextHolder.getLocale())));
        }
    }

    @GetMapping("/getRoles")
    public ResponseEntity<ResponseMessage<List<Role>>> getAllRole() {
        List<Role> listRole;
        try {
            listRole = iRoleRepository.findAll();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseMessage(messageSource.getMessage("ROLES_GET_ALL_FAILED",
                            null,
                            LocaleContextHolder.getLocale()),HTTP_NOT_FOUND));
        }
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseMessage(messageSource.getMessage("ROLES_GET_ALL_SUCCESS",
                        null,
                        LocaleContextHolder.getLocale()),HTTP_OK ,listRole ));
    }

}
