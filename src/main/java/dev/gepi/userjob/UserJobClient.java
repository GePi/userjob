package dev.gepi.userjob;

import dev.gepi.userjob.api.dto.UserJobDTO;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;


public class UserJobClient {

    private static final String BASE_URL = "http://localhost:8080/api/v1/";

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

        ResponseEntity<List<String>> response = restTemplate.exchange(BASE_URL + "update-userjob", HttpMethod.PATCH, request, new ParameterizedTypeReference<List<String>>() {
        });
        if (response.getStatusCode().is2xxSuccessful()) {
            System.out.println("Patched.");
            List<String> rates = response.getBody();

        } else {
            System.out.println("Failed to save user job.");
        }
    }

    private ClientHttpRequestFactory getClientHttpRequestFactory() {
        int timeout = 5000;
        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory
                = new HttpComponentsClientHttpRequestFactory();
        clientHttpRequestFactory.setConnectTimeout(timeout);
        return clientHttpRequestFactory;
    }

    public void getUserJob() {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        UserJobDTO.Users user = new UserJobDTO.Users();
        //user.setUserId(1L);
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(BASE_URL + "get-userjob")
                .queryParam("user", user);

        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<UserJobDTO[]> response = restTemplate.getForEntity(builder.toUriString(), UserJobDTO[].class);

        if (response.getStatusCode().is2xxSuccessful()) {
            UserJobDTO[] userJobDTOS = response.getBody();
            return; // Arrays.asList(userJobRequests);
        } else {
            System.out.println("Failed to get user job.");
            return; // Collections.emptyList();
        }
    }
}