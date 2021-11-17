package com.sellics.keyword.score;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import com.sellics.keyword.score.amazon.client.AmazonClient;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author Pankaj Singh
 *
 */
@SpringBootApplication
@EnableFeignClients(clients = AmazonClient.class)
@EnableSwagger2
public class SellicsApplication {

	public static void main(String[] args) {
		SpringApplication.run(SellicsApplication.class, args);
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

}
