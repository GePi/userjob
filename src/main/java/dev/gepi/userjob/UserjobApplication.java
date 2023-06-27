package dev.gepi.userjob;

import dev.gepi.userjob.api.dto.UserJobDTO;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class UserjobApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserjobApplication.class, args);

        UserJobDTO userJobDTO = new UserJobDTO();
        userJobDTO.getCompany().setCompanyName("Компания 1");
        userJobDTO.getUsers().setFirstName("Сергей");
        userJobDTO.getUserJobInfo().setIsActivity(true);
        UserJobClient userJobClient = new UserJobClient();
        userJobClient.postUserJob(userJobDTO);
        //userJobClient.getUserJob();

        userJobDTO.getUsers().setUserId(1L);
        userJobDTO.getCompany().setIdCompany(1L);
        userJobDTO.getUserJobInfo().setDescription("Измененная Description");
        userJobClient.patchUserJob(userJobDTO);
    }

}
