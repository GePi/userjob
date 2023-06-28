package dev.gepi.userjob;

import dev.gepi.userjob.example.UserJobClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class UserjobApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserjobApplication.class, args);

        UserJobClient userJobClient = new UserJobClient();
        userJobClient.run();
    }
}
