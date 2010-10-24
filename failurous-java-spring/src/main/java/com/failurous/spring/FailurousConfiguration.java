package com.failurous.spring;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

import com.failurous.FailSender;

@Configuration
@ImportResource("classpath:/failurous-properties-config.xml")
public class FailurousConfiguration {

	private @Value("${failurous.server.address}") String serverAddress;
	private @Value("${failurous.api.key}") String apiKey;
	
	public @Bean FailSender failSender() {
		FailSender sender = new FailSender();
		sender.init(serverAddress, apiKey);
		return sender;
	}
	
}
