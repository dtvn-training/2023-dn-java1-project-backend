package com.example.project.controller;

import com.example.project.dto.UserCreateDTO;
import com.example.project.dto.UserDTO;
import com.example.project.model.User;
import com.example.project.responses.UserResponse;
import com.example.project.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
    @GetMapping("") // http://localhost:8080/api/v1/products?page=1&limit=10
    public ResponseEntity<UserResponse> getProducts(@RequestParam("page") int page, @RequestParam("limit") int limit) {
        // page start 0 -> limit
        // get và sắp sếp số lượng sản phẩm theo trang và số lượng.
        PageRequest pageRequest = PageRequest.of(page, limit, Sort.by("createAt").descending());
        // get product
        Page<UserResponse> userPage = userService.getAllUser(pageRequest);
        // get tổng số trang
        int totalPage = userPage.getTotalPages();
        // get số sản phẩm đã được tính theo trang
        List<UserResponse> products = userPage.getContent();
        // map
        UserResponse userResponse = UserResponse.builder()
                .userResponses(products)
                .totalPage(totalPage)
                .build();
        return ResponseEntity.ok(userResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProduct(@PathVariable Long id) {
        try {
            User user = userService.getUserByID(id);
            return ResponseEntity.ok(UserResponse.mapUser(user));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }
    @PostMapping("")
    public ResponseEntity<?> createProduct(@RequestBody @Valid UserCreateDTO request, BindingResult result) {
        try {
            // kiểm tra dữ liệu
            if (result.hasErrors()) {
                List<String> errMessage = result.getAllErrors()
                        .stream()
                        .map(fieldError -> fieldError.getDefaultMessage())
                        .toList();
                return ResponseEntity.badRequest().body(errMessage);
            }
            request.setRoleId(1L);
            UserDTO newUser = userService.createUser(request);
            // trả về ok nếu vượt qua kiểm tra
            return ResponseEntity.ok("Create user success");
        } catch (Exception e) {
            System.out.println(e.toString());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(@Valid @PathVariable Long id, @Valid @PathVariable UserDTO request) {
        try {
            return ResponseEntity.ok("update user successfully = " + request);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.ok("Delete product successfully id = " + id);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
