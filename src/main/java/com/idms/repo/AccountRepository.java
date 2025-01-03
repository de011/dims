package com.idms.repo;

import com.idms.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Integer> {
    boolean existsByAcctId(Integer acctId);
}
