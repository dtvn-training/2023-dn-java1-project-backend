package com.example.project.controller;

import com.example.project.responses.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/user")
public class UserController {
    @GetMapping("") // http://localhost:8080/api/v1/products?page=1&limit=10
    public ResponseEntity<UserResponse> getProducts(@RequestParam("page") int page, @RequestParam("limit") int limit) {
        // page start 0 -> limit
        // get và sắp sếp số lượng sản phẩm theo trang và số lượng.
        PageRequest pageRequest = PageRequest.of(page, limit, Sort.by("createAt").descending());
        // get product
        Page<UserResponse> productPage = productService.getAllProducts(pageRequest);
        // get tổng số trang
        int totalPage = productPage.getTotalPages();
        // get số sản phẩm đã được tính theo trang
        List<UserResponse> products = productPage.getContent();
        // map
        UserResponse productResponses = UserResponse.builder()
                .UserResponse(products)
                .totalPage(totalPage)
                .build();
        return ResponseEntity.ok(productResponses);
    }
}
