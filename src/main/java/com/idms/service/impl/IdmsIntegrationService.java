package com.idms.service;

import com.idms.entity.Account;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class IdmsIntegrationService {

    private final RestTemplate restTemplate;

    public IdmsIntegrationService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<Account> fetchAccountsFromIdms(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<Account[]> response = restTemplate.exchange(
                "https://idms.dealersocket.com/api/account/GetAccountList",
                HttpMethod.GET,
                entity,
                Account[].class
        );
        return Arrays.asList(response.getBody());
    }
}