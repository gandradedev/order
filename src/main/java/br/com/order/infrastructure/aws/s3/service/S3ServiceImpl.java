package br.com.order.infrastructure.aws.s3.service;

import br.com.order.core.properties.AwsConfigurationProperties;
import br.com.order.infrastructure.aws.s3.exception.S3IntegrationException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.core.exception.SdkServiceException;
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
                           final MultipartFile file) {
        try {
            log.info("Starting S3 integration to upload image {} to key {}",
                fileName,
                key);

            Path tempFile = Files.createTempFile(TEMP_FILE_PREFIX, fileName);
            Files.write(tempFile, file.getBytes());

            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(properties.getS3().bucketName())
                .key(key)
                .build();

            s3Client.putObject(putObjectRequest, tempFile);

            log.info("Image {} uploaded successfully to key {}", fileName, key);

            Files.delete(tempFile);
        } catch (IOException e) {
            log.error("Error creating temporary file to upload {} image", fileName, e);
            throw new S3IntegrationException("Error creating temporary file before uploading to S3");
        } catch (SdkClientException | SdkServiceException e) {
            log.error("Integration with S3 to upload {} image received the error: {}",
                fileName,
                e.getMessage());
            throw new S3IntegrationException("Error received when uploading image to S3");
        }
    }

    @Override
    public URL getObjectUrl(final String key) {
        try {
            log.info("Starting S3 integration to get object url from key {}", key);

            GetUrlRequest request = GetUrlRequest.builder()
                .bucket(properties.getS3().bucketName())
                .key(key)
                .build();

            URL url = s3Client.utilities().getUrl(request);

            log.info("Successfully obtained the object's URL from key {}", key);

            return url;
        } catch (SdkClientException | SdkServiceException e) {
            log.error("Integration with S3 to get object url {} received the error: {}",
                key,
                e.getMessage());
            throw new S3IntegrationException("Error received when getting object URL in S3");
        }
    }

    @Override
    public void deleteFile(final String key) {
        try {
            log.info("Starting S3 integration to delete image from key {}", key);

            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(properties.getS3().bucketName())
                .key(key)
                .build();

            s3Client.deleteObject(deleteObjectRequest);

            log.info("Image {} deleted successfully from S3", key);
        } catch (SdkClientException | SdkServiceException e) {
            log.error("Integration with S3 to delete key {} received the error: {}",
                key,
                e.getMessage());
            throw new S3IntegrationException("Error received to delete image");
        }

    }
}
