package com.wg_planner.backend.Service;

import com.wg_planner.backend.Repository.AccountRepository;
import com.wg_planner.backend.Repository.RoomRepository;
import com.wg_planner.backend.entity.Account;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
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

//    @PostConstruct
//    public void populateTestData() {
//        if(accountRepository.count() == 0) {
//            List<Account> accounts = new ArrayList<>();
//            for (int i = 0; i < 9; i++) {
//                accounts.add(
//                        (Account) Account.withUsername(i + "@example.com")
//                                .password("{noop}password")
//                                .roles("USER")
//                                .build());
//            }
//            accountRepository.saveAll(accounts);
//            List<Account> accounts1 = accountRepository.findAll();
//            List<Account> accounts2 = accountRepository.findAll();
//        }
//    }

    @Override
    public UserDetails loadUserByUsername(final String username)
            throws UsernameNotFoundException {
        Validate.notNull(username, "parameter username must not be %s", null);
        Validate.notEmpty(username, "parameter username must not be empty");
        return accountRepository.findAccountByUsername(username);
    }
}
