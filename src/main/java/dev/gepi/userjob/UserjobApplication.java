package dev.gepi.userjob;

import dev.gepi.userjob.api.UserJobRequestDTO;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class UserjobApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserjobApplication.class, args);

        UserJobRequestDTO userJobRequestDTO = new UserJobRequestDTO();
        userJobRequestDTO.getCompany().setCompanyName("Компания 1");
        userJobRequestDTO.getUsers().setFirstName("Сергей");
        userJobRequestDTO.getUserJobInfo().setIsActivity(true);
        UserJobClient userJobClient = new UserJobClient();
        userJobClient.postUserJob(userJobRequestDTO);
        //userJobClient.getUserJob();
    }

}
