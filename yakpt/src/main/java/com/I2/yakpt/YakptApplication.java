package com.I2.yakpt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class YakptApplication {

	public static void main(String[] args) {
		System.setProperty("java.rmi.server.hostname", "127.0.0.1");
		System.setProperty("h2.bindAddress", "127.0.0.1");

		SpringApplication.run(YakptApplication.class, args);
	}
}
