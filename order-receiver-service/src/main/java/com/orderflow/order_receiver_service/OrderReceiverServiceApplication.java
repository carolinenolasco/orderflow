package com.orderflow.order_receiver_service;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableFeignClients(basePackages = "com.orderflow.order_receiver_service.client")
@SpringBootApplication
public class OrderReceiverServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrderReceiverServiceApplication.class, args);
	}
}

