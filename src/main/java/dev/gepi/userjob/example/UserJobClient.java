package dev.gepi.userjob.example;

import dev.gepi.userjob.api.dto.UserJobDTO;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public class UserJobClient {

    private static final String BASE_URL = "http://localhost:8080/api/v1/";

    public void run() {
        UserJobDTO userJobDTO = new UserJobDTO();
        userJobDTO.getCompany().setCompanyName("Компания 1");
        userJobDTO.getUsers().setFirstName("Сергей");
        userJobDTO.getUserJobInfo().setIsActivity(true);

        postUserJob(userJobDTO);

        userJobDTO.getUsers().setUserId(1L);
        userJobDTO.getCompany().setIdCompany(1L);
        userJobDTO.getUserJobInfo().setDescription("Измененная Description");

        patchUserJob(userJobDTO);
    }

    public void postUserJob(UserJobDTO userJobDTO) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<UserJobDTO> request = new HttpEntity<>(userJobDTO, headers);
        ResponseEntity<Void> response = restTemplate.postForEntity(BASE_URL + "create-userjob", request, Void.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            System.out.println("User job saved successfully.");
        } else {
            System.out.println("Failed to save user job.");
        }
    }

    public void patchUserJob(UserJobDTO userJobDTO) {
        RestTemplate restTemplate = new RestTemplate();
        HttpClient httpClient = HttpClientBuilder.create().build();

        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(httpClient));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<UserJobDTO> request = new HttpEntity<>(userJobDTO, headers);

        ResponseEntity<List<String>> response = restTemplate.exchange(BASE_URL + "update-userjob", HttpMethod.PATCH, request, new ParameterizedTypeReference<>() {
        });
        if (response.getStatusCode().is2xxSuccessful()) {
            System.out.println("Patched.");
            List<String> rates = response.getBody();
            if (rates != null) {
                rates.forEach(System.out::println);
            }
        } else {
            System.out.println("Failed to save user job.");
        }
    }
}