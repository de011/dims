package com.idms.controller;

import com.idms.dto.*;
import com.idms.entity.*;
import com.idms.exception.InsuranceServiceException;
import com.idms.exception.InvalidDateFormatException;
import com.idms.exception.SideNoteServiceException;
import com.idms.exception.UnauthorizedAccessException;
import com.idms.service.*;
import com.idms.service.impl.AccountService;
import com.idms.utility.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private InsuranceService insuranceService;


    @Autowired
    private BulkInsuranceService bulkInsuranceService;


    @Autowired
    private SideNoteService sideNoteService;


    @Autowired
    private AccountInfoService accountInfoService;

    @Autowired
    private QueueService queueService;

    @Autowired
    private SalesLocationService salesLocationService;


    /*@PostMapping("/fetch")
    public ResponseEntity<?> fetchAccounts(@RequestHeader(value = "Authorization") String bearerToken) {
        if (bearerToken == null || !bearerToken.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Missing or invalid Authorization header");
        }

        try {
            accountService.fetchAndSaveAccounts(bearerToken.replace("Bearer ", ""));
            return ResponseEntity.ok("Accounts fetched and saved successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/GetAccountList")
    public ResponseEntity<?> getAllAccounts(@RequestHeader(value = "Authorization") String bearerToken) {
        if (bearerToken == null || !bearerToken.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Missing or invalid Authorization header");
        }

        try {
            List<Account> accounts = accountService.getAllAccounts();
            return accounts.isEmpty() ? ResponseEntity.ok("No accounts found.") : ResponseEntity.ok(accounts);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    @GetMapping("/GetAccountLists")
    public ResponseEntity<?> getAccountList(
            @RequestParam String Token,
            @RequestParam String LayoutID,
            @RequestParam String PageNumber,
            @RequestParam String AccountStatus,
            @RequestParam String InstitutionID) {

        if ("mock-token".equals(Token)) {
            return ResponseEntity.ok(Map.of(
                    "Status", "200",
                    "Message", "Success",
                    "Data", List.of(
                            Map.ofEntries(
                                    Map.entry("acctId", 12345),
                                    Map.entry("contractSalesPrice", 25000.00),
                                    Map.entry("acctType", "Loan"),
                                    Map.entry("salesGroupPerson1Id", 10),
                                    Map.entry("contractDate", "2023-12-15"),
                                    Map.entry("collateralStockNumber", "ABC123"),
                                    Map.entry("collateralYearModel", 2023),
                                    Map.entry("collateralMake", "Toyota"),
                                    Map.entry("collateralModel", "Camry"),
                                    Map.entry("borrower1FirstName", "John"),
                                    Map.entry("borrower1LastName", "Doe")
                            )
                    )
            ));
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
                "Status", "401",
                "Message", "Invalid Token"
        ));
    }

*/
    @PostMapping("/fetch")
    public ResponseEntity<String> fetchAccounts(@RequestHeader(value = "Authorization", required = false) String bearerToken) {
        if (bearerToken == null || !bearerToken.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Missing or invalid Authorization header. Expected format: 'Bearer <token>'");
        }

        try {
            String token = bearerToken.replace("Bearer ", "");
            System.out.println("Token: " + token);

            String username = jwtUtil.extractUsername(token);
            System.out.println("Extracted Username: " + username);

            if (!jwtUtil.validateToken(token, username)) {
                System.out.println("Token is invalid or expired");
                return ResponseEntity.status(UNAUTHORIZED).body("Invalid or expired token");
            }

            accountService.fetchAndSaveAccounts(token);
            return ResponseEntity.ok("Accounts fetched and saved successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body("Error fetching accounts: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body("Unexpected error occurred: " + e.getMessage());
        }
    }

    @GetMapping("/GetAccountList")
    public ResponseEntity<?> getAllAccounts(@RequestHeader(value = "Authorization", required = false) String bearerToken) {
        try {

            if (bearerToken == null || !bearerToken.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Missing or invalid Authorization header. Expected format: 'Bearer <token>'");
            }

            String token = bearerToken.replace("Bearer ", "");
            String username = jwtUtil.extractUsername(token);

            if (!jwtUtil.validateToken(token, username)) {
                return ResponseEntity.status(UNAUTHORIZED).body("Invalid or expired token");
            }


            List<Account> accounts = accountService.getAllAccounts();

            if (accounts.isEmpty()) {
                return ResponseEntity.status(HttpStatus.OK).body("No accounts found in the database.");
            }

            return ResponseEntity.ok(accounts);
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body("An error occurred while retrieving accounts: " + e.getMessage());
        }
    }
    /*@GetMapping("/GetAccountLists")
    public ResponseEntity<?> getAccountList(
            @RequestParam String Token,
            @RequestParam String LayoutID,
            @RequestParam String PageNumber,
            @RequestParam String AccountStatus,
            @RequestParam String InstitutionID) {

        if ("mock-token".equals(Token)) {
            return ResponseEntity.ok(Map.of(
                    "Status", "200",
                    "Message", "Success",
                    "Data", List.of(
                            Map.ofEntries(
                                    Map.entry("acctId", 12345),
                                    Map.entry("contractSalesPrice", 25000.00),
                                    Map.entry("acctType", "Loan"),
                                    Map.entry("salesGroupPerson1Id", 10),
                                    Map.entry("contractDate", "2023-12-15"),
                                    Map.entry("collateralStockNumber", "ABC123"),
                                    Map.entry("collateralYearModel", 2023),
                                    Map.entry("collateralMake", "Toyota"),
                                    Map.entry("collateralModel", "Camry"),
                                    Map.entry("borrower1FirstName", "John"),
                                    Map.entry("borrower1LastName", "Doe")
                            )
                    )
            ));
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
                "Status", "401",
                "Message", "Invalid Token"
        ));
    }*/

    @GetMapping("/GetAccountLists")
    public ResponseEntity<?> getAccountList(@RequestParam String Token, @RequestParam String LayoutID, @RequestParam String PageNumber, @RequestParam String AccountStatus, @RequestParam String InstitutionID) {

        if (!accountService.isValidToken(Token)) {
            return ResponseEntity.status(UNAUTHORIZED).body(Map.of("Status", "401", "Message", "Invalid Token"));
        }

        List<Account> accountData = accountService.getMockAccounts();

        return ResponseEntity.ok(Map.of("Status", "200", "Message", "Success", "Data", accountData));
    }


    // Another PDF requirements


    @PostMapping("/AddOrUpdateInsuranceInfo")
    public ResponseEntity<?> addOrUpdateInsuranceInfo(@RequestHeader("Authorization") String token, @RequestBody InsuranceInfoDTO insuranceInfoDTO) {
        try {
            InsuranceInfo insuranceInfo = insuranceService.addOrUpdateInsurance(insuranceInfoDTO);

            // Wrap the response in a custom structure with a success message
            return ResponseEntity.ok(new ApiResponse<>("Insurance information updated successfully.", insuranceInfo));
        } catch (InsuranceServiceException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>(e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse<>("An unexpected error occurred: " + e.getMessage(), null));
        }
    }

    @GetMapping("/GetAllInsuranceInfo")
    public ResponseEntity<?> getAllInsuranceInfo(@RequestHeader("Authorization") String token) {
        try {
            List<InsuranceInfo> insuranceInfoList = insuranceService.getAllInsuranceInfo();

            if (insuranceInfoList.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new ApiResponse<>("No insurance information found.", null));
            }
            return ResponseEntity.ok(new ApiResponse<>("Insurance information retrieved successfully.", insuranceInfoList));
        } catch (UnauthorizedAccessException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ApiResponse<>("Unauthorized access: " + e.getMessage(), null));
        } catch (InsuranceServiceException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>("Error fetching insurance information: " + e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse<>("An unexpected error occurred: " + e.getMessage(), null));
        }
    }

    @PostMapping("/BulkInsuranceInputs")
    public ResponseEntity<String> addOrUpdateBulkInsurance(@RequestHeader("Authorization") String token, @RequestBody List<BulkInsuranceDTO> bulkInsuranceDTOs) {

        try {
            bulkInsuranceService.addOrUpdateBulkInsurance(bulkInsuranceDTOs);
            return ResponseEntity.ok("Bulk insurance inputs added/updated successfully.");
        } catch (InsuranceServiceException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        } catch (InvalidDateFormatException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid date format: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body("An unexpected error occurred: " + e.getMessage());
        }
    }

    @GetMapping("/getInsuranceInputList")
    public ResponseEntity<?> getAllBulkInsuranceInputs(@RequestHeader("Authorization") String token) {

        try {
            List<BulkInsuranceInput> inputs = bulkInsuranceService.getAllBulkInsuranceInputs();

            if (inputs.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No bulk insurance records found.");
            }

            return ResponseEntity.ok(inputs);
        } catch (InsuranceServiceException e) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body("An unexpected error occurred: " + e.getMessage());
        }
    }

    @PostMapping("/CreateSideNote")
    public ResponseEntity<?> addOrUpdateSideNote(@RequestHeader("Authorization") String token, @RequestBody SideNoteDTO sideNoteDTO) {
        try {
            if (sideNoteDTO == null || sideNoteDTO.getAccountId() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Account ID cannot be null.");
            }

            SideNote sideNote = sideNoteService.addOrUpdateSideNote(sideNoteDTO);
            return ResponseEntity.ok(sideNote);
        } catch (SideNoteServiceException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error adding/updating side note: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body("An unexpected error occurred: " + e.getMessage());
        }
    }


    @GetMapping("/GetSideNotes")
    public ResponseEntity<?> getSideNotesByAccountId(@RequestHeader("Authorization") String token, @RequestParam Integer accountId) {
        try {
            if (accountId == null || accountId <= 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Account ID.");
            }

            List<SideNote> sideNotes = sideNoteService.getSideNotesByAccountId(accountId);
            if (sideNotes.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No side notes found for the provided Account ID.");
            }
            return ResponseEntity.ok(sideNotes);
        } catch (SideNoteServiceException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error fetching side notes: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body("An unexpected error occurred: " + e.getMessage());
        }
    }

    @GetMapping("/AllSideNotes")
    public ResponseEntity<?> getAllSideNotes(@RequestHeader("Authorization") String token) {
        try {
            List<SideNote> sideNotes = sideNoteService.getAllSideNotes();
            if (sideNotes.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No side notes found.");
            }
            return ResponseEntity.ok(sideNotes);
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body("An unexpected error occurred: " + e.getMessage());
        }
    }

    @GetMapping("/GetAccountInfo")
    public ResponseEntity<AccountInfoDTO> getAccountInfo(@RequestHeader("Authorization") String token, @RequestParam Integer accountId) {
        AccountInfoDTO accountInfo = accountInfoService.getAccountInfo(accountId);
        return ResponseEntity.ok(accountInfo);
    }

    @GetMapping("/GetQueueList")
    public ResponseEntity<List<QueueDTO>> getQueueList(@RequestHeader("Authorization") String token) {
        List<QueueDTO> queueList = queueService.getQueueList();
        return ResponseEntity.ok(queueList);
    }

    @PostMapping("/AddOrUpdateSalesLocation")
    public ResponseEntity<ApiResponse<?>> addOrUpdateSalesLocation(@RequestHeader("Authorization") String token, @RequestBody @Valid SalesLocationDTO salesLocationDTO) {

        if (token == null || token.isEmpty()) {
            return ResponseEntity.status(UNAUTHORIZED).body(new ApiResponse<>("Authorization token is missing or invalid.", null));
        }

        try {
            SalesLocation salesLocation = salesLocationService.addOrUpdateSalesLocation(salesLocationDTO);
            ApiResponse<SalesLocation> response = new ApiResponse<>("Sales location created/updated successfully with ID: " + salesLocation.getSalesLocationId(), salesLocation);
            return ResponseEntity.status(CREATED).body(response);
        } catch (Exception ex) {
            ApiResponse<String> errorResponse = new ApiResponse<>("An error occurred while processing your request: " + ex.getMessage(), null);
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/GetSalesLocations")
    public ResponseEntity<ApiResponse<?>> getAllSalesLocations(@RequestHeader("Authorization") String token) {

        if (token == null || token.isEmpty()) {
            return ResponseEntity.status(UNAUTHORIZED).body(new ApiResponse<>("Authorization token is missing or invalid.", null));
        }

        try {
            List<SalesLocation> salesLocations = salesLocationService.getAllSalesLocations();
            if (salesLocations.isEmpty()) {
                ApiResponse<String> noContentResponse = new ApiResponse<>("No sales locations found.", null);
                return ResponseEntity.status(NO_CONTENT).body(noContentResponse);
            }
            ApiResponse<List<SalesLocation>> successResponse = new ApiResponse<>("Sales locations retrieved successfully.", salesLocations);
            return ResponseEntity.ok(successResponse);
        } catch (Exception ex) {
            ApiResponse<String> errorResponse = new ApiResponse<>("An error occurred while retrieving the sales locations: " + ex.getMessage(), null);
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }


    @GetMapping("/GetSalesLocation/{id}")
    public ResponseEntity<ApiResponse<?>> getSalesLocationById(@RequestHeader("Authorization") String token, @PathVariable Integer id) {

        if (token == null || token.isEmpty()) {
            return ResponseEntity.status(UNAUTHORIZED).body(new ApiResponse<>("Authorization token is missing or invalid.", null));
        }

        try {
            SalesLocation salesLocation = salesLocationService.getSalesLocationById(id);
            ApiResponse<SalesLocation> successResponse = new ApiResponse<>("SalesLocation found for ID: " + id, salesLocation);
            return ResponseEntity.ok(successResponse);
        } catch (RuntimeException ex) {
            ApiResponse<String> notFoundResponse = new ApiResponse<>("SalesLocation not found for ID: " + id, null);
            return ResponseEntity.status(NOT_FOUND).body(notFoundResponse);
        } catch (Exception ex) {
            ApiResponse<String> errorResponse = new ApiResponse<>("An error occurred while retrieving the sales location: " + ex.getMessage(), null);
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}