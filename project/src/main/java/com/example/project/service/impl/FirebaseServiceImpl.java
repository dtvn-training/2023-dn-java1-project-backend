package com.example.project.service.impl;

import com.example.project.service.IFirebaseService;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.google.firebase.internal.FirebaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

import static com.example.project.constants.Constants.*;
@Service
@RequiredArgsConstructor
public class FirebaseServiceImpl implements IFirebaseService {

    private final MessageSource messageSource;
    @Override
    public String uploadFile(MultipartFile multipartFile) throws IOException {
        String objectName = generateFileName(multipartFile);

        try (InputStream fileInputStream = multipartFile.getInputStream()) {
            FileInputStream serviceAccount = new FileInputStream(messageSource.getMessage(FIREBASE_SDK_JSON, null, LocaleContextHolder.getLocale()));

            Storage storage = StorageOptions.newBuilder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setProjectId(messageSource.getMessage(FIREBASE_PROJECT_ID, null, LocaleContextHolder.getLocale()))
                    .build()
                    .getService();

            BlobId blobId = BlobId.of(messageSource.getMessage(FIREBASE_BUCKET, null, LocaleContextHolder.getLocale()), objectName);
            BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                    .setContentType(multipartFile.getContentType())
                    .build();

            storage.create(blobInfo, fileInputStream);

            String fileUrl = "https://firebasestorage.googleapis.com/v0/b/" + messageSource.getMessage(FIREBASE_BUCKET, null, LocaleContextHolder.getLocale()) +
                    "/o/" + objectName +
                    "?alt=media&token=" + generateToken();

            return  fileUrl;
        }
    }

    @Override
    public String generateToken() {
        return UUID.randomUUID().toString();
    }

    @Override
    public String generateFileName(MultipartFile multiPart) {
        return new Date().getTime() + "-" + Objects.requireNonNull(multiPart.getOriginalFilename()).replace(" ", "_");
    }
}
