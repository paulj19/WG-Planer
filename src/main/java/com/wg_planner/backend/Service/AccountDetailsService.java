package com.wg_planner.backend.Service;

import com.wg_planner.backend.Repository.AccountRepository;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service("accountDetailsService")
public class AccountDetailsService implements UserDetailsService {

    private static final Logger LOGGER = Logger.getLogger(AccountDetailsService.class
            .getName());

    private final AccountRepository accountRepository;

    @Autowired
    public AccountDetailsService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public UserDetails loadUserByUsername(final String username)
            throws UsernameNotFoundException {
        Validate.notNull(username, "parameter username must not be %s", null);
        Validate.notEmpty(username, "parameter username must not be empty");
        return accountRepository.findAccountByUsername(username);
    }

    public boolean isUsernameUnique(String username) {
        return accountRepository.findAccountByUsername(username) == null;
    }

}
