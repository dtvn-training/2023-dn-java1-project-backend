package com.example.project.service.impl;

import com.example.project.service.IFirebaseService;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.google.firebase.internal.FirebaseService;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

public class FirebaseServiceImpl implements IFirebaseService {
    @Override
    public String uploadFile(MultipartFile multipartFile) throws IOException {
        String objectName = generateFileName(multipartFile);

        try (InputStream fileInputStream = multipartFile.getInputStream()) {
            FileInputStream serviceAccount = new FileInputStream(FIREBASE_SDK_JSON);

            Storage storage = StorageOptions.newBuilder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setProjectId(FIREBASE_PROJECT_ID)
                    .build()
                    .getService();

            BlobId blobId = BlobId.of(FIREBASE_BUCKET, objectName);
            BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                    .setContentType(multipartFile.getContentType())
                    .build();

            storage.create(blobInfo, fileInputStream);

            String fileUrl = "https://firebasestorage.googleapis.com/v0/b/" + FIREBASE_BUCKET +
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
