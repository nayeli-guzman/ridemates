package com.ridemates.app.storage.domain;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.ridemates.app.user.domain.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.Principal;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class StorageService {

    private final UserService userService;

    @Value("${application.bucket.name}")
    private String bucketName;

    @Autowired
    private AmazonS3 s3Client;

    public void uploadProfile(@RequestBody MultipartFile file, Principal principal) throws Exception {
        if (file == null) {
            throw new IllegalArgumentException("Invalid file");
        }

        String username = principal.getName();

        String objectKey = "profile/" + username;
        String fileKey = uploadFile(file, objectKey);

        var user = userService.findMeUser();
        user.setProfileKey(fileKey);
        userService.save(user);
    }

    public String uploadFile(MultipartFile file, String objectKey) throws Exception {
        if (file.isEmpty() || file.getSize() > 5242880)
            throw new IllegalArgumentException("Invalid file size");

        if (objectKey.startsWith("/") || !objectKey.contains("."))
            throw new IllegalArgumentException("Invalid object key");

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());

        if (s3Client.doesObjectExist(bucketName, objectKey))
            deleteFile(objectKey);

        try (InputStream inputStream = file.getInputStream()) {
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, objectKey, inputStream, metadata);
            s3Client.putObject(putObjectRequest);
            return objectKey;
        } catch (IOException e) {
            throw new Exception("Failed to upload file to S3", e);
        }
    }

    public String generatePresignedUrl(String objectKey) {
        if (!s3Client.doesObjectExist(bucketName, objectKey))
            throw new RuntimeException("File not found in S3");

        Date expiration = new Date(System.currentTimeMillis() + 1000 * 60 * 60);
        GeneratePresignedUrlRequest generatePresignedUrlRequest =
                new GeneratePresignedUrlRequest(bucketName, objectKey)
                        .withExpiration(expiration);

        URL url = s3Client.generatePresignedUrl(generatePresignedUrlRequest);
        return url.toString();
    }

    public String getProfilePic(Principal principal) {
        var user = userService.findByEmail(principal.getName());
        return user.getProfileKey() == null ? null : generatePresignedUrl(user.getProfileKey());
    }

    public boolean deleteProfilePic(Principal principal) {
        var user = userService.findByEmail(principal.getName());
        if (user.getProfileKey() == null) return false;

        deleteFile(user.getProfileKey());
        user.setProfileKey(null);
        userService.save(user);
        return true;
    }

    public void deleteFile(String objectKey) {
        s3Client.deleteObject(bucketName, objectKey);
    }

}
