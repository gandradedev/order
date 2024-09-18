package br.com.order.core.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "aws")
public class AwsConfigurationProperties {

    private String region;

    private Credentials credentials;

    private S3 s3;

    public record Credentials(String accessKeyId, String secretAccessKey) { }

    public record S3(String bucketName) { }
}
