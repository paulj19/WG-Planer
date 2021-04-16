package com.wg_planner.views.utils;

import com.wg_planner.backend.Service.ResidentAccountService;
import com.wg_planner.backend.entity.ResidentAccount;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

@Controller
@Scope("prototype")
public class AccountDetailsHelper {
    protected static final java.util.logging.Logger LOGGER = java.util.logging.Logger.getLogger(AccountDetailsHelper.class
            .getName());

    private AccountDetailsHelper() {
    }

    public static ResidentAccount getUserResidentAccount(ResidentAccountService residentAccountService) {
        return residentAccountService.getResidentAccountByUsername(getLoggedInUserName());
    }


    private static String getLoggedInUserName() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof org.springframework.security.core.userdetails.UserDetails) {
            return ((org.springframework.security.core.userdetails.UserDetails) principal).getUsername();
        } else {
//            LOGGER.log(Level.SEVERE, "Logged in Account: " + ((org.springframework.security.core.userdetails.UserDetails) principal).toString());
            throw new IllegalStateException("finding user name from getUserName failed");
        }
    }

}
