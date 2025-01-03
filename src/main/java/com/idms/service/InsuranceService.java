package com.idms.service;

import com.idms.dto.InsuranceInfoDTO;
import com.idms.entity.InsuranceInfo;

import java.util.List;

public interface InsuranceService {
    InsuranceInfo addOrUpdateInsurance(InsuranceInfoDTO dto);

    List<InsuranceInfo> getAllInsuranceInfo();

}
