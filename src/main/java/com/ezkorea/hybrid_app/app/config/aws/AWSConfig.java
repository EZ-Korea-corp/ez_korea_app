package com.ezkorea.hybrid_app.app.config.aws;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class AWSConfig {

    @Value("${aws.iam.access_key}")
    private final String iamAccessKey;

    @Value("${aws.iam.secret_key}")
    private final String iamSecretKey;

    @Value("${aws.iam.region}")
    private final String region;

    @Bean
    public AmazonS3Client amazonS3Client() {
        BasicAWSCredentials credentials = new BasicAWSCredentials(iamAccessKey, iamSecretKey);
        return (AmazonS3Client) AmazonS3ClientBuilder.standard()
                .withRegion(region)
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .build();
    }



}
