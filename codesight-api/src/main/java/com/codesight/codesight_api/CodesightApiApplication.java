package com.codesight.codesight_api;

import com.codesight.codesight_api.infrastructure.controllerAdvices.ChallengeControllerAdvice;
import com.codesight.codesight_api.web.controllers.ChallengeController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
public class CodesightApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(CodesightApiApplication.class, args);
    }

}
