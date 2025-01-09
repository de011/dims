package com.idms.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.idms.entity.Account;
import com.idms.repo.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${mock.token}")
    private String validToken;

    public boolean isValidToken(String token) {
        return validToken.equals(token);
    }

    public List<Account> getMockAccounts() {
        return accountRepository.findAll(); // Fetch from the database
    }

    public void fetchAndSaveAccounts(String jwtToken) {
        try {
            String accountListUrl = "http://localhost:8080/api/accounts/GetAccountLists?Token=mock-token&LayoutID=2006084&PageNumber=1&AccountStatus=a&InstitutionID=107007";

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + jwtToken);

            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<String> response = restTemplate.exchange(accountListUrl, HttpMethod.GET, entity, String.class);

            if (response.getStatusCode() != HttpStatus.OK) {
                throw new RuntimeException("Failed to fetch accounts: " + response.getBody());
            }

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());

            JsonNode rootNode = objectMapper.readTree(response.getBody());
            if (!"200".equals(rootNode.get("Status").asText())) {
                throw new RuntimeException("Error fetching accounts: " + rootNode.get("Message").asText());
            }

            JsonNode dataNode = rootNode.get("Data");
            if (dataNode == null || !dataNode.isArray()) {
                throw new RuntimeException("No account data found in the API response.");
            }

            List<Account> accounts = objectMapper.readerForListOf(Account.class).readValue(dataNode);
            accounts.forEach(account -> {
                if (!accountRepository.existsById(account.getAcctId())) {
                    accountRepository.save(account);
                }
            });
        } catch (Exception e) {
            throw new RuntimeException("Error processing accounts: " + e.getMessage(), e);
        }
    }

    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }
}