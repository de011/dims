package com.idms.repo;
import com.idms.entity.BulkInsuranceInput;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface BulkInsuranceInputRepository extends JpaRepository<BulkInsuranceInput, Integer> {
    @Query("SELECT b FROM BulkInsuranceInput b WHERE b.accountId IN :accountIds AND b.policyNumber IN :policyNumbers")
    List<BulkInsuranceInput> findByAccountIdAndPolicyNumberIn(List<Integer> accountIds, List<String> policyNumbers);

}