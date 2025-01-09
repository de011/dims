package com.idms.controller;

import com.idms.dto.*;
import com.idms.entity.*;
import com.idms.exception.InsuranceServiceException;
import com.idms.exception.InvalidDateFormatException;
import com.idms.exception.SideNoteServiceException;
import com.idms.exception.UnauthorizedAccessException;
import com.idms.service.*;
import com.idms.service.impl.AccountService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;

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

    @PostMapping("/fetch")
    public ResponseEntity<String> fetchAccounts() {
        // The token is already validated by the JwtAuthenticationFilter
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String token = authentication.getCredentials().toString(); // Get the token if needed
        accountService.fetchAndSaveAccounts(token);
        return ResponseEntity.ok("Accounts fetched and saved successfully.");
    }

    @GetMapping("/GetAccountList")
    public ResponseEntity<List<Account>> getAllAccounts() {
        List<Account> accounts = accountService.getAllAccounts();
        return ResponseEntity.ok(accounts);
    }

    @PostMapping("/AddOrUpdateInsuranceInfo")
    public ResponseEntity<ApiResponse<?>> addOrUpdateInsuranceInfo(@RequestBody @Valid InsuranceInfoDTO insuranceInfoDTO) {
        try {
            // Optionally, you can access the authenticated user here
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName(); // Get the authenticated username

            InsuranceInfo insuranceInfo = insuranceService.addOrUpdateInsurance(insuranceInfoDTO);
            return ResponseEntity.ok(new ApiResponse<>("Insurance information updated successfully.", insuranceInfo));
        } catch (InsuranceServiceException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>(e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse<>("An unexpected error occurred: " + e.getMessage(), null));
        }
    }

    @GetMapping("/GetAllInsuranceInfo")
    public ResponseEntity<ApiResponse<?>> getAllInsuranceInfo() {
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
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse<>("An unexpected error occurred: " + e.getMessage(), null));
        }
    }

    @PostMapping("/BulkInsuranceInputs")
    public ResponseEntity<String> addOrUpdateBulkInsurance(@RequestBody List<BulkInsuranceDTO> bulkInsuranceDTOs) {
        try {
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
    public ResponseEntity<?> getAllBulkInsuranceInputs() {
        try {
            List<BulkInsuranceInput> inputs = bulkInsuranceService.getAllBulkInsuranceInputs();
            if (inputs.isEmpty()){
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
    public ResponseEntity<?> addOrUpdateSideNote(@RequestBody SideNoteDTO sideNoteDTO) {
        try {
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
    public ResponseEntity<?> getSideNotesByAccountId(@RequestParam Integer accountId) {
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
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred: " + e.getMessage());
        }
    }

    @GetMapping("/AllSideNotes")
    public ResponseEntity<?> getAllSideNotes() {
        try {
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
    public ResponseEntity<AccountInfoDTO> getAccountInfo(@RequestParam Integer accountId) {
        try {
            AccountInfoDTO accountInfo = accountInfoService.getAccountInfo(accountId);
            return ResponseEntity.ok(accountInfo);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/GetQueueList")
    public ResponseEntity<List<QueueDTO>> getQueueList() {
        try {
            List<QueueDTO> queueList = queueService.getQueueList();
            return ResponseEntity.ok(queueList);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/AddOrUpdateSalesLocation")
    public ResponseEntity<ApiResponse<?>> addOrUpdateSalesLocation(@RequestBody @Valid SalesLocationDTO salesLocationDTO) {
        try {
            SalesLocation salesLocation = salesLocationService.addOrUpdateSalesLocation(salesLocationDTO);
            ApiResponse<SalesLocation> response = new ApiResponse<>("Sales location created/updated successfully with ID: " + salesLocation.getSalesLocationId(), salesLocation);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception ex) {
            ApiResponse<String> errorResponse = new ApiResponse<>("An error occurred while processing your request: " + ex.getMessage(), null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/GetSalesLocations")
    public ResponseEntity<ApiResponse<?>> getAllSalesLocations() {
        try {
            List<SalesLocation> salesLocations = salesLocationService.getAllSalesLocations();
            if (salesLocations.isEmpty()) {
                ApiResponse<String> noContentResponse = new ApiResponse<>("No sales locations found.", null);
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(noContentResponse);
            }
            ApiResponse<List<SalesLocation>> successResponse = new ApiResponse<>("Sales locations retrieved successfully.", salesLocations );
            return ResponseEntity.ok(successResponse);
        } catch (Exception ex) {
            ApiResponse<String> errorResponse = new ApiResponse<>("An error occurred while retrieving the sales locations: " + ex.getMessage(), null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/GetSalesLocation/{id}")
    public ResponseEntity<ApiResponse<?>> getSalesLocationById(@PathVariable Integer id) {
        try {
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