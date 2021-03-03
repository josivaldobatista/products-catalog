package com.jfb.productscatalog;

import com.jfb.productscatalog.services.S3Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ProductsCatalogApplication implements CommandLineRunner {

	@Autowired
	private S3Service s3Service;

	public static void main(String[] args) {
		SpringApplication.run(ProductsCatalogApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		s3Service.uploadFile("/Users/valdo/test.jpg");

	}

}
