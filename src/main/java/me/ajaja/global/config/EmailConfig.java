package me.ajaja.global.config;

import java.util.Properties;

import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
@Import(MailProperties.class)
public class EmailConfig {

	@Bean
	public JavaMailSender javaMailSender(MailProperties mailProperties) {
		JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();

		javaMailSender.setHost(mailProperties.getHost());
		javaMailSender.setPort(mailProperties.getPort());
		javaMailSender.setUsername(mailProperties.getUsername());
		javaMailSender.setPassword(mailProperties.getPassword());
		javaMailSender.setJavaMailProperties(javaMailProperties(mailProperties));

		return javaMailSender;
	}

	private Properties javaMailProperties(MailProperties mailProperties) {
		Properties properties = new Properties();
		properties.putAll(mailProperties.getProperties());
		return properties;
	}
}
