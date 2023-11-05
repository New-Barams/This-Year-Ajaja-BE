package com.newbarams.ajaja.infra.ses;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;

@Configuration
public class AwsSesConfig {
	@Value("${ses.credentials.access-key}")
	private String accessKey;

	@Value("${ses.credentials.secret-key}")
	private String secretKey;

	@Bean
	AmazonSimpleEmailService amazonSimpleEmailService() {
		return AmazonSimpleEmailServiceClientBuilder.standard()
			.withCredentials(generateCredentialsProvider())
			.withRegion(Regions.AP_NORTHEAST_2)
			.build();
	}

	private AWSStaticCredentialsProvider generateCredentialsProvider() {
		BasicAWSCredentials basicAwsCredentials = new BasicAWSCredentials(accessKey, secretKey);
		return new AWSStaticCredentialsProvider(basicAwsCredentials);
	}
}
