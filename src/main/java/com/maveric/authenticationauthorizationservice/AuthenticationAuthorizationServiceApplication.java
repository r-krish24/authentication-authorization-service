package com.maveric.authenticationauthorizationservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
@EnableEurekaClient
public class AuthenticationAuthorizationServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthenticationAuthorizationServiceApplication.class, args);
	}

}
