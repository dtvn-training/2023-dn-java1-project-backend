package com.example.project.controller;

import com.example.project.constants.AppContants;
import com.example.project.dto.UserCreateDTO;
import com.example.project.dto.UserDTO;
import com.example.project.exception.ResponseMessage;
import com.example.project.model.User;
import com.example.project.payload.response.ListUserResponse;
import com.example.project.payload.response.UserCreateResponse;
import com.example.project.payload.response.UserResponse;
import com.example.project.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/users")
public class UserController {
    private final UserService userService;

    // Get list user
    @GetMapping("") // http://localhost:3000/api/users?page=1&limit=5
    public ResponseEntity<ListUserResponse> getUsers(@RequestParam("page") int page, @RequestParam("limit") int limit) {
        PageRequest pageRequest = PageRequest.of(page, limit, Sort.by("createdAt").descending());
        Page<UserResponse> userPage = userService.getAllUsers(pageRequest);
        int totalPage = userPage.getTotalPages();
        List<UserResponse> users = userPage.getContent();
        ListUserResponse listUserResponse = ListUserResponse.builder()
                .listUser(users)
                .totalPage(totalPage)
                .build();
        return ResponseEntity.ok(listUserResponse);
    }

    // create user
    @PostMapping("")
    public ResponseEntity<?> createUser(@RequestBody @Valid UserCreateDTO request, BindingResult result) {
        try {
            if (result.hasErrors()) {
                List<String> errMessage = result.getAllErrors()
                        .stream()
                        .map(fieldError -> fieldError.getDefaultMessage())
                        .toList();
                return ResponseEntity.badRequest().body(errMessage);
            }
            UserDTO newUser = userService.createUser(request);
            var userModel = UserCreateResponse.builder()
                    .firstName(newUser.getFirstName())
                    .lastName(newUser.getLastName())
                    .email(newUser.getEmail())
                    .roleId(newUser.getRoleId())
                    .address(newUser.getAddress())
                    .phone(newUser.getPhone())
                    .build();
            return ResponseEntity.ok(userModel);
        } catch (Exception e) {
            System.out.println(e.toString());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    // Get single user
    @GetMapping("/{id}")
    public ResponseEntity<?> getUser(@PathVariable Long id) {
        try {
            User user = userService.getUserByID(id);
            return ResponseEntity.ok(UserResponse.mapUser(user));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }
    //Update user
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@Valid @PathVariable Long id, @Valid @RequestBody UserDTO request) {
        UserDTO accountUpdated = userService.updateUser(id, request);
        if (accountUpdated != null) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseMessage(AppContants.ACCOUNT_UPDATE_SUCCESS, AppContants.ACCOUNT_SUCCESS_CODE,accountUpdated));
        } else {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseMessage(AppContants.ACCOUNT_NOT_FOUND, AppContants.RESOURCE_NOT_FOUND_CODE));
        }

    }
    // Delete User
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.ok("Delete product successfully id = " + id);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
