package com.idms.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.idms.entity.Account;
import com.idms.repo.AccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class AccountService {
    private static final Logger logger = LoggerFactory.getLogger(AccountService.class);
    @Autowired
    private AccountRepository accountRepository;

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${bearer.token}")
    private String validToken;

    public boolean isValidToken(String token) {
        return validToken.equals(token);
    }

    /**
     * Fetches account data from an external API and saves it to the local database.
     *
     * @param jwtToken the JSON Web Token used for authentication with the external API
     */
    public void fetchAndSaveAccounts(String jwtToken) {
        //Replace here the external API
        String accountListUrl = "http://localhost:8080/api/accounts/GetAccountLists?Token=bearer-token&LayoutID=2006084&PageNumber=1&AccountStatus=a&InstitutionID=107007";

        HttpEntity<String> entity = createHttpEntity(jwtToken);
        ResponseEntity<String> response = makeApiCall(accountListUrl, entity);

        validateResponse(response);

        List<Account> accounts = parseAccountData(response.getBody());
        saveAccounts(accounts);
    }

    private HttpEntity<String> createHttpEntity(String jwtToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwtToken);
        return new HttpEntity<>(headers);
    }

    private ResponseEntity<String> makeApiCall(String url, HttpEntity<String> entity) {
        return restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
    }

    private void validateResponse(ResponseEntity<String> response) {
        if (response.getStatusCode() != HttpStatus.OK) {
            throw new RuntimeException("Failed to fetch accounts: " + response.getBody());
        }
    }

    private List<Account> parseAccountData(String responseBody) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());

            JsonNode rootNode = objectMapper.readTree(responseBody);
            JsonNode statusNode = rootNode.get("Status");
            if (statusNode == null || !"200".equals(statusNode.asText())) {
                String errorMessage = "Error fetching accounts: ";
                JsonNode messageNode = rootNode.get("Message");
                errorMessage += (messageNode != null) ? messageNode.asText() : "No message provided"; // Default message if none is found
                throw new RuntimeException(errorMessage);
            }

            JsonNode dataNode = rootNode.get("Data");
            if (dataNode == null || !dataNode.isArray()) {
                throw new RuntimeException("No account data found in the API response.");
            }

            return objectMapper.readerForListOf(Account.class).readValue(dataNode);
        } catch (Exception e) {
            throw new RuntimeException("Error parsing account data: " + e.getMessage(), e);
        }
    }

    private void saveAccounts(List<Account> accounts) {
        accounts.forEach(account -> {
            if (!accountRepository.existsById(account.getAcctId())) {
                accountRepository.save(account);
            }
        });
    }
    public List<Account> getAllAccounts() {
        try {
            List<Account> accounts = accountRepository.findAll();
            logger.info("Fetched {} accounts from the database.", accounts.size());
            return accounts;
        } catch (Exception e) {
            logger.error("Error fetching accounts from the database: {}", e.getMessage());
            throw new RuntimeException("Failed to retrieve accounts. Please try again later.", e);
        }
   }
}