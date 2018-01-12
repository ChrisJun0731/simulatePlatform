package com.genture.simulatePlatform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Created by Administrator on 2018/1/5.
 */
@SpringBootApplication
@EnableScheduling
public class SimulatePlatformApplication {
	public static void main(String[] args){
		SpringApplication.run(SimulatePlatformApplication.class);
	}
}
