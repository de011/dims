package com.idms.service;

import com.idms.dto.BulkInsuranceDTO;
import com.idms.entity.BulkInsuranceInput;

import java.util.List;
import java.util.Optional;

public interface BulkInsuranceService {

    List<BulkInsuranceInput> addOrUpdateBulkInsurance(List<BulkInsuranceDTO> bulkInsuranceDTOs);
    List<BulkInsuranceInput> getAllBulkInsuranceInputs();
    //Optional<BulkInsuranceInput> findByAccountIdAndPolicyNumber(Long accountId, String policyNumber);


}
