package com.idms.service.impl;

import com.idms.dto.AccountInfoDTO;
import com.idms.entity.Account;
import com.idms.repo.AccountRepository;
import com.idms.service.AccountInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountInfoServiceImpl implements AccountInfoService {
    @Autowired
    private AccountRepository accountRepository;

    @Override
    public AccountInfoDTO getAccountInfo(Integer accountId) {
        Account account = accountRepository.findById(accountId).orElseThrow(() -> new RuntimeException("Account not found"));
        AccountInfoDTO accountInfoDTO = new AccountInfoDTO();
        accountInfoDTO.setId(account.getAcctId());
        accountInfoDTO.setBorrower(account.getBorrower1FirstName());
        accountInfoDTO.setBalance(account.getBalance());
        return accountInfoDTO;
    }
}
