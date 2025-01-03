package com.idms.service.impl;

import com.idms.dto.InsuranceInfoDTO;
import com.idms.entity.InsuranceInfo;
import com.idms.exception.DuplicateInsuranceRecordException;
import com.idms.exception.InsuranceServiceException;
import com.idms.repo.InsuranceRepository;
import com.idms.service.InsuranceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class InsuranceServiceImpl implements InsuranceService {

    @Autowired
    private InsuranceRepository insuranceRepository;

    @Override
    @Transactional
    public InsuranceInfo addOrUpdateInsurance(InsuranceInfoDTO dto) {
        try {

            Optional<InsuranceInfo> existingInfo = insuranceRepository.findByAccountIdAndPolicyNumber(dto.getAccountId(), dto.getPolicyNumber());

            if (existingInfo.isPresent()) {
                throw new DuplicateInsuranceRecordException("Duplicate insurance record found for Account ID: " + dto.getAccountId() + " and Policy Number: " + dto.getPolicyNumber());
            }

            InsuranceInfo insuranceInfo = new InsuranceInfo();
            insuranceInfo.setAccountId(dto.getAccountId());
            insuranceInfo.setInsuranceCompany(dto.getInsuranceCompany());
            insuranceInfo.setPolicyNumber(dto.getPolicyNumber());
            insuranceInfo.setStartDate(dto.getStartDate());
            insuranceInfo.setEndDate(dto.getEndDate());
            insuranceInfo.setCoverageAmount(dto.getCoverageAmount());
            return insuranceRepository.save(insuranceInfo);

        } catch (DuplicateInsuranceRecordException e) {
            throw e;
        } catch (DataIntegrityViolationException e) {

            if (e.getCause() instanceof org.hibernate.exception.ConstraintViolationException) {
                throw new InsuranceServiceException("Unique constraint violation: Duplicate accountId and policyNumber.", e);
            }
            throw new InsuranceServiceException("Database constraint violation occurred. Please check for unique constraints or NULL values.", e);
        } catch (IllegalArgumentException e) {
            throw new InsuranceServiceException("Invalid input provided", e);
        } catch (Exception e) {
            throw new InsuranceServiceException("An unexpected error occurred", e);
        }
    }


    @Override
    public List<InsuranceInfo> getAllInsuranceInfo() {
        try {
            List<InsuranceInfo> insuranceInfoList = insuranceRepository.findAll();

            if (insuranceInfoList.isEmpty()) {
                throw new InsuranceServiceException("No insurance information found.");
            }

            return insuranceInfoList;
        } catch (Exception e) {
            throw new InsuranceServiceException("An error occurred while retrieving insurance information: " + e.getMessage(), e);
        }
    }

}
