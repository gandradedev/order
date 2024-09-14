package br.com.order.infrastructure.aws.s3.service;

import java.io.IOException;
import java.net.URL;

public interface S3Service {

    void uploadFile(String fileName, String key, byte[] fileContent) throws IOException;

    URL getObjectUrl(String key);

    void deleteFile(String key);
}
