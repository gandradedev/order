package br.com.order.infrastructure.aws.s3.service;

import br.com.order.core.properties.AwsConfigurationProperties;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

@Slf4j
@Service
@AllArgsConstructor
@EnableConfigurationProperties(AwsConfigurationProperties.class)
public class S3ServiceImpl implements S3Service {

    private static final String TEMP_FILE_PREFIX = "s3-temp-";

    private final S3Client s3Client;

    private final AwsConfigurationProperties properties;


    @Override
    public void uploadFile(final String fileName,
                           final String key,
                           final byte[] fileContent) throws IOException {
        Path tempFile = Files.createTempFile(TEMP_FILE_PREFIX, fileName);
        Files.write(tempFile, fileContent);

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
            .bucket(properties.getS3().bucketName())
            .key(key)
            .build();

        s3Client.putObject(putObjectRequest, tempFile);

        Files.delete(tempFile);
    }

    @Override
    public URL getObjectUrl(final String key) {
        GetUrlRequest request = GetUrlRequest.builder()
            .bucket(properties.getS3().bucketName())
            .key(key)
            .build();

        return s3Client.utilities().getUrl(request);
    }

    @Override
    public void deleteFile(final String key) {
        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
            .bucket(properties.getS3().bucketName())
            .key(key)
            .build();

        s3Client.deleteObject(deleteObjectRequest);
    }
}
