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

/*    public void fetchAndSaveAccounts(String token) {
        try {
            String url = "https://idms.dealersocket.com/api/Account/GetAccountList" +
                    "?Token=" + token +
                    "&LayoutID=2006084&PageNumber=1&AccountStatus=a&InstitutionID=107007";

            // Fetch raw JSON response
            String response = restTemplate.getForObject(url, String.class);

            // Parse the response as a JSON object
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(response);

            // Check the "Status" field in the response
            if (rootNode.has("Status") && rootNode.get("Status").asText().equals("404")) {
                throw new RuntimeException("Invalid token: " + rootNode.get("Message").asText());
            }

            // Extract the "Data" node
            JsonNode dataNode = rootNode.get("Data");
            if (dataNode == null || !dataNode.isArray()) {
                throw new RuntimeException("No account data found in the API response.");
            }

            // Deserialize the "Data" array to a list of Account objects
            List<Account> accounts = objectMapper.readerForListOf(Account.class).readValue(dataNode);

            // Save only new accounts
            accounts.forEach(account -> {
                if (!accountRepository.existsById(account.getAcctId())) {
                    accountRepository.save(account);
                }
            });
        } catch (Exception e) {
            throw new RuntimeException("Error processing accounts from the external API: " + e.getMessage(), e);
        }
    }*/

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
            // Step 1: Authenticate and fetch data
            String accountListUrl = "http://localhost:8080/api/accounts/GetAccountLists?Token=mock-token&LayoutID=2006084&PageNumber=1&AccountStatus=a&InstitutionID=107007";

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + jwtToken);

            HttpEntity<String> entity = new HttpEntity<>(headers);
            RestTemplate restTemplate = new RestTemplate();

            ResponseEntity<String> response = restTemplate.exchange(accountListUrl, HttpMethod.GET, entity, String.class);

            if (response.getStatusCode() != HttpStatus.OK) {
                throw new RuntimeException("Failed to fetch accounts: " + response.getBody());
            }

            // Use custom ObjectMapper
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
