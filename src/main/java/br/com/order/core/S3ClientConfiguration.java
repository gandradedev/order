package br.com.order.core;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;

import java.net.URI;

@Configuration
public class S3ClientConfiguration {

    @Value("${aws.credentials.accessKeyId}")
    private String awsAccessKeyId;

    @Value("${aws.credentials.secretAccessKey}")
    private String awsSecretAccessKey;

    @Value("${aws.region}")
    private String awsRegion;

    @Value("${localstack.endpoint:null}")
    private String localStackEndpointOverride;

    @Bean
    @Profile("!development")
    public S3Client s3Client() {
        return S3Client.builder()
            .region(Region.of(awsRegion))
            .credentialsProvider(StaticCredentialsProvider.create(
                AwsBasicCredentials.create(
                        awsAccessKeyId,
                        awsSecretAccessKey)
            )).build();
    }

    @Bean
    @Profile("development")
    public S3Client s3DevelopmentClient() {
        return S3Client.builder()
                .region(Region.of(awsRegion))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(
                                awsAccessKeyId,
                                awsSecretAccessKey
                        )
                ))
                .endpointOverride(URI.create(localStackEndpointOverride))
                .serviceConfiguration(S3Configuration.builder()
                        .pathStyleAccessEnabled(true)
                        .build())
                .build();
    }

}
