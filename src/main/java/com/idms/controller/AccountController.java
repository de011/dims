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

    // A utility function to extract and validate token
    private String extractAndValidateToken(String bearerToken) {
        if (bearerToken == null || !bearerToken.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Missing or invalid Authorization header.");
        }
        String token = bearerToken.substring(7);
        if (!jwtUtil.validateToken(token, jwtUtil.extractUsername(token))) {
            throw new IllegalArgumentException("Invalid or expired token");
        }
        return token; // Return the valid token
    }

    @PostMapping("/fetch")
    public ResponseEntity<String> fetchAccounts(@RequestHeader("Authorization") String bearerToken) {
        try {
            String token = extractAndValidateToken(bearerToken);
            accountService.fetchAndSaveAccounts(token);
            return ResponseEntity.ok("Accounts fetched and saved successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/GetAccountList")
    public ResponseEntity<List<Account>> getAllAccounts(@RequestHeader("Authorization") String bearerToken) {
        try {
            String token = extractAndValidateToken(bearerToken);
            List<Account> accounts = accountService.getAllAccounts();
            return ResponseEntity.ok(accounts);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PostMapping("/AddOrUpdateInsuranceInfo")
    public ResponseEntity<ApiResponse<?>> addOrUpdateInsuranceInfo(@RequestHeader("Authorization") String bearerToken, @RequestBody @Valid InsuranceInfoDTO insuranceInfoDTO) {
        try {
            String token = extractAndValidateToken(bearerToken);
            InsuranceInfo insuranceInfo = insuranceService.addOrUpdateInsurance(insuranceInfoDTO);
            return ResponseEntity.ok(new ApiResponse<>("Insurance information updated successfully.", insuranceInfo));
        } catch (InsuranceServiceException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>(e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse<>("An unexpected error occurred: " + e.getMessage(), null));
        }
    }

    @GetMapping("/GetAllInsuranceInfo")
    public ResponseEntity<ApiResponse<?>> getAllInsuranceInfo(@RequestHeader("Authorization") String bearerToken) {
        try {
            String token = extractAndValidateToken(bearerToken);
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
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse<>("An unexpected error occurred: " + e.getMessage(), null));
        }
    }

    @PostMapping("/BulkInsuranceInputs")
    public ResponseEntity<String> addOrUpdateBulkInsurance(@RequestHeader("Authorization") String bearerToken, @RequestBody List<BulkInsuranceDTO> bulkInsuranceDTOs) {
        try {
            String token = extractAndValidateToken(bearerToken);
            bulkInsuranceService.addOrUpdateBulkInsurance(bulkInsuranceDTOs);
            return ResponseEntity.ok("Bulk insurance inputs added/updated successfully.");
        } catch (InsuranceServiceException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        } catch (InvalidDateFormatException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid date format: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred: " + e.getMessage());
        }
    }

    @GetMapping("/getInsuranceInputList")
    public ResponseEntity<?> getAllBulkInsuranceInputs(@RequestHeader("Authorization") String bearerToken) {
        try {
            String token = extractAndValidateToken(bearerToken);
            List<BulkInsuranceInput> inputs = bulkInsuranceService.getAllBulkInsuranceInputs();
            if (inputs.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No bulk insurance records found.");
            }
            return ResponseEntity.ok(inputs);
        } catch (InsuranceServiceException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred: " + e.getMessage());
        }
    }

    @PostMapping("/CreateSideNote")
    public ResponseEntity<?> addOrUpdateSideNote(@RequestHeader("Authorization") String bearerToken, @RequestBody SideNoteDTO sideNoteDTO) {
        try {
            String token = extractAndValidateToken(bearerToken);
            if (sideNoteDTO == null || sideNoteDTO.getAccountId() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Account ID cannot be null.");
            }
            SideNote sideNote = sideNoteService.addOrUpdateSideNote(sideNoteDTO);
            return ResponseEntity.ok(sideNote);
        } catch (SideNoteServiceException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error adding/updating side note: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred: " + e.getMessage());
        }
    }

    @GetMapping("/GetSideNotes")
    public ResponseEntity<?> getSideNotesByAccountId(@RequestHeader("Authorization") String bearerToken, @RequestParam Integer accountId) {
        try {
            String token = extractAndValidateToken(bearerToken);
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
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred: " + e.getMessage());
        }
    }

    @GetMapping("/AllSideNotes")
    public ResponseEntity<?> getAllSideNotes(@RequestHeader("Authorization") String bearerToken) {
        try {
            String token = extractAndValidateToken(bearerToken);
            List<SideNote> sideNotes = sideNoteService.getAllSideNotes();
            if (sideNotes.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No side notes found.");
            }
            return ResponseEntity.ok(sideNotes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred: " + e.getMessage());
        }
    }

    @GetMapping("/GetAccountInfo")
    public ResponseEntity<AccountInfoDTO> getAccountInfo(@RequestHeader("Authorization") String bearerToken, @RequestParam Integer accountId) {
        try {
            String token = extractAndValidateToken(bearerToken);
            AccountInfoDTO accountInfo = accountInfoService.getAccountInfo(accountId);
            return ResponseEntity.ok(accountInfo);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/GetQueueList")
    public ResponseEntity<List<QueueDTO>> getQueueList(@RequestHeader("Authorization") String bearerToken) {
        try {
            String token = extractAndValidateToken(bearerToken);
            List<QueueDTO> queueList = queueService.getQueueList();
            return ResponseEntity.ok(queueList);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/AddOrUpdateSalesLocation")
    public ResponseEntity<ApiResponse<?>> addOrUpdateSalesLocation(@RequestHeader("Authorization") String bearerToken, @RequestBody @Valid SalesLocationDTO salesLocationDTO) {
        try {
            String token = extractAndValidateToken(bearerToken);
            SalesLocation salesLocation = salesLocationService.addOrUpdateSalesLocation(salesLocationDTO);
            ApiResponse<SalesLocation> response = new ApiResponse<>("Sales location created/updated successfully with ID: " + salesLocation.getSalesLocationId(), salesLocation);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception ex) {
            ApiResponse<String> errorResponse = new ApiResponse<>("An error occurred while processing your request: " + ex.getMessage(), null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/GetSalesLocations")
    public ResponseEntity<ApiResponse<?>> getAllSalesLocations(@RequestHeader("Authorization") String bearerToken) {
        try {
            String token = extractAndValidateToken(bearerToken);
            List<SalesLocation> salesLocations = salesLocationService.getAllSalesLocations();
            if (salesLocations.isEmpty()) {
                ApiResponse<String> noContentResponse = new ApiResponse<>("No sales locations found.", null);
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(noContentResponse);
            }
            ApiResponse<List<SalesLocation>> successResponse = new ApiResponse<>("Sales locations retrieved successfully.", salesLocations);
            return ResponseEntity.ok(successResponse);
        } catch (Exception ex) {
            ApiResponse<String> errorResponse = new ApiResponse<>("An error occurred while retrieving the sales locations: " + ex.getMessage(), null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/GetSalesLocation/{id}")
    public ResponseEntity<ApiResponse<?>> getSalesLocationById(@RequestHeader("Authorization") String bearerToken, @PathVariable Integer id) {
        try {
            String token = extractAndValidateToken(bearerToken);
            SalesLocation salesLocation = salesLocationService.getSalesLocationById(id);
            ApiResponse<SalesLocation> successResponse = new ApiResponse<>("SalesLocation found for ID: " + id, salesLocation);
            return ResponseEntity.ok(successResponse);
        } catch (RuntimeException ex) {
            ApiResponse<String> notFoundResponse = new ApiResponse<>("SalesLocation not found for ID: " + id, null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(notFoundResponse);
        } catch (Exception ex) {
            ApiResponse<String> errorResponse = new ApiResponse<>("An error occurred while retrieving the sales location: " + ex.getMessage(), null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}