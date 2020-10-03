package com.wg_planner.backend.Service;

import com.wg_planner.backend.Repository.ResidentAccountRepository;
import com.wg_planner.backend.entity.Account;
import com.wg_planner.backend.entity.ResidentAccount;
import org.springframework.beans.factory.annotation.Autowired;

public class ResidentAccountService {
    private final ResidentAccountRepository residentAccountRepository;

    @Autowired
    public ResidentAccountService(ResidentAccountRepository residentAccountRepository) {
        this.residentAccountRepository = residentAccountRepository;
    }

    public ResidentAccount getResidentAccount(Long accountId) {
        return residentAccountRepository.getResidentAccount(accountId);
    }
}
