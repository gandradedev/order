package br.com.order.infrastructure.aws.s3.exception;

public class S3IntegrationException extends RuntimeException {

    public S3IntegrationException(final String message) {
        super(message);
    }
}
