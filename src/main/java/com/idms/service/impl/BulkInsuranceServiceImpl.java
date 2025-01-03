package com.idms.service.impl;

import com.idms.dto.BulkInsuranceDTO;
import com.idms.entity.BulkInsuranceInput;
import com.idms.exception.DuplicateInsuranceRecordException;
import com.idms.exception.InsuranceServiceException;
import com.idms.exception.InvalidDateFormatException;
import com.idms.repo.BulkInsuranceInputRepository;
import com.idms.service.BulkInsuranceService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class BulkInsuranceServiceImpl implements BulkInsuranceService {
    @Autowired
    private BulkInsuranceInputRepository bulkInsuranceInputRepository;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public List<BulkInsuranceInput> addOrUpdateBulkInsurance(List<BulkInsuranceDTO> bulkInsuranceDTOs) {
        if (bulkInsuranceDTOs == null || bulkInsuranceDTOs.isEmpty()) {
            throw new InsuranceServiceException("Input list cannot be null or empty.");
        }

        List<BulkInsuranceInput> savedEntities = new ArrayList<>();
        try {

            Set<String> uniqueKeys = bulkInsuranceDTOs.stream()
                    .map(dto -> dto.getAccountId() + "-" + dto.getPolicyNumber())
                    .collect(Collectors.toSet());

            List<Integer> accountIds = uniqueKeys.stream()
                    .map(key -> Integer.parseInt(key.split("-")[0]))
                    .collect(Collectors.toList());

            List<String> policyNumbers = uniqueKeys.stream()
                    .map(key -> key.split("-")[1])
                    .collect(Collectors.toList());
            List<BulkInsuranceInput> existingRecords = bulkInsuranceInputRepository.findByAccountIdAndPolicyNumberIn(accountIds, policyNumbers);

            Set<String> existingKeys = existingRecords.stream()
                    .map(record -> record.getAccountId() + "-" + record.getPolicyNumber())
                    .collect(Collectors.toSet());

            for (BulkInsuranceDTO dto : bulkInsuranceDTOs) {
                String key = dto.getAccountId() + "-" + dto.getPolicyNumber();

                if (existingKeys.contains(key)) {
                    throw new DuplicateInsuranceRecordException(
                            "Duplicate insurance record found for Account ID: " + dto.getAccountId() + " and Policy Number: " + dto.getPolicyNumber()
                    );
                }

                BulkInsuranceInput input = new BulkInsuranceInput();
                input.setAccountId(dto.getAccountId());
                input.setInsuranceProvider(dto.getInsuranceProvider());
                input.setPolicyNumber(dto.getPolicyNumber());

                try {
                    input.setEffectiveDate(LocalDate.parse(dto.getEffectiveDate(), formatter));
                    input.setExpirationDate(LocalDate.parse(dto.getExpirationDate(), formatter));
                } catch (Exception e) {
                    throw new InvalidDateFormatException("Invalid date format. Expected yyyy-MM-dd.", e);
                }

                savedEntities.add(bulkInsuranceInputRepository.save(input));
            }
        } catch (DuplicateInsuranceRecordException e) {
            throw e;
        } catch (Exception e) {
            throw new InsuranceServiceException("Error while processing bulk insurance data: " + e.getMessage(), e);
        }

        return savedEntities;
    }


    @Override
    public List<BulkInsuranceInput> getAllBulkInsuranceInputs() {
        try {
            List<BulkInsuranceInput> inputs = bulkInsuranceInputRepository.findAll();
            if (inputs.isEmpty()) {
                throw new InsuranceServiceException("No insurance records found.");
            }
            return inputs;
        } catch (Exception e) {
            throw new InsuranceServiceException("Error retrieving insurance records: " + e.getMessage(), e);
        }
    }

}
