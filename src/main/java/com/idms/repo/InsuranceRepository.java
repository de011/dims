package com.idms.repo;

import com.idms.entity.InsuranceInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InsuranceRepository extends JpaRepository<InsuranceInfo,Long> {
    Optional<InsuranceInfo> findByAccountId(Long accountId);
    Optional<InsuranceInfo> findByAccountIdAndPolicyNumber(Long accountId, String policyNumber);
}
