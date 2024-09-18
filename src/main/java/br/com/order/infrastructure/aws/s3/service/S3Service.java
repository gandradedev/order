package br.com.order.infrastructure.aws.s3.service;

import org.springframework.web.multipart.MultipartFile;

import java.net.URL;

public interface S3Service {

    void uploadFile(String fileName, String key, MultipartFile file);

    URL getObjectUrl(String key);

    void deleteFile(String key);
}
