package com.monora.personalbothub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PersonalBotHubApplication {

    public static void main(String[] args) {
        SpringApplication.run(PersonalBotHubApplication.class, args);
    }

}
