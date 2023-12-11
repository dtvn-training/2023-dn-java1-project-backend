package com.example.project.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public class IFirebaseService {
    String uploadFile(MultipartFile multipartFile) throws IOException;
    String generateToken();
    String generateFileName(MultipartFile multiPart);
}
