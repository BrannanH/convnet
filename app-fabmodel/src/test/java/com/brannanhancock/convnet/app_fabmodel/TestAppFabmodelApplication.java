package com.brannanhancock.convnet.app_fabmodel;

import org.springframework.boot.SpringApplication;

public class TestAppFabmodelApplication {

	public static void main(String[] args) {
		SpringApplication.from(AppFabmodelApplication::main)/*.with(TestcontainersConfiguration.class)*/.run(args);
	}

}
