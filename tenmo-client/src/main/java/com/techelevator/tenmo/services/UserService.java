package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.User;
import com.techelevator.util.BasicLogger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.security.DrbgParameters;
import java.util.ArrayList;
import java.util.List;

public class UserService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String baseUrl = "http://localhost:8080/users/";

    private String authToken = null;


    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public UserService() {
    }


    public User[] getUsers() {

        User[] userArray = null;
        String url = baseUrl;

        try {
            ResponseEntity<User[]> response = restTemplate.exchange(url, HttpMethod.GET, makeAuthEntity(), User[].class);

            userArray = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return userArray;
    }


    private HttpEntity<Void> makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        return new HttpEntity<>(headers);
    }

}
