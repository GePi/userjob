package dev.gepi.userjob;

import dev.gepi.userjob.api.UserJobRequestDTO;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

public class UserJobClient {

    private static final String BASE_URL = "http://localhost:8080/api/v1/";

    public void postUserJob(UserJobRequestDTO userJobRequestDTO) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<UserJobRequestDTO> request = new HttpEntity<>(userJobRequestDTO, headers);
        ResponseEntity<Void> response = restTemplate.postForEntity(BASE_URL + "create-userjob", request, Void.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            System.out.println("User job saved successfully.");
        } else {
            System.out.println("Failed to save user job.");
        }
    }

    public void getUserJob() {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        UserJobRequestDTO.Users user = new UserJobRequestDTO.Users();
        //user.setUserId(1L);
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(BASE_URL + "get-userjob")
                .queryParam("user", user);

        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<UserJobRequestDTO[]> response = restTemplate.getForEntity(builder.toUriString(), UserJobRequestDTO[].class);

        if (response.getStatusCode().is2xxSuccessful()) {
            UserJobRequestDTO[] userJobRequestDTOS = response.getBody();
            return; // Arrays.asList(userJobRequests);
        } else {
            System.out.println("Failed to get user job.");
            return; // Collections.emptyList();
        }
    }
}