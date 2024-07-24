package com.example.simple_stock_management;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.example.simple_stock_management.repository")
@EntityScan(basePackages = "com.example.simple_stock_management.model")
public class SimpleStockManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(SimpleStockManagementApplication.class, args);
	}

}
