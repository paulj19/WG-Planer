package com.wg_planner.views.utils;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.server.VaadinSession;
import com.wg_planner.backend.Service.ResidentAccountService;
import com.wg_planner.backend.entity.ResidentAccount;
import com.wg_planner.backend.utils.LogHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

@Controller
@Scope("prototype")
public class AccountDetailsHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(AccountDetailsHelper.class);
    @Autowired
    private ResidentAccountService residentAccountService;

    private static AccountDetailsHelper accountDetailsHelper;

//    static {
//        accountDetailsHelper = new AccountDetailsHelper();
//    }

    public static void setAccountDetailsHelper(AccountDetailsHelper accountDetailsHelper) {
        AccountDetailsHelper.accountDetailsHelper = accountDetailsHelper;
    }

    public static AccountDetailsHelper getInstance() {
        return accountDetailsHelper;
    }

    public AccountDetailsHelper() {
    }

    public ResidentAccount getLoggedInResidentAccount() {
        return residentAccountService.getResidentAccountByUsername(getLoggedInUserName());
    }

    private static String getLoggedInUserName() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof org.springframework.security.core.userdetails.UserDetails) {
            LOGGER.info(LogHandler.getTestRun(), "getLoggedInUserName Resident Account username {}.",
                    ((org.springframework.security.core.userdetails.UserDetails) principal).getUsername());
            return ((org.springframework.security.core.userdetails.UserDetails) principal).getUsername();
        } else {
//            LOGGER.log(Level.SEVERE, "Logged in Account: " + ((org.springframework.security.core.userdetails.UserDetails) principal).toString());
            throw new IllegalStateException("finding user name from getUserName failed");
        }
    }
    public static void logoutAndNavigateToLoginPage() {
        LOGGER.info(LogHandler.getTestRun(), "Logging out resident Account id {}.",
                SessionHandler.getLoggedInResidentAccount().getId());
        UI.getCurrent().getSession().close();
        // Close the VaadinServiceSession
        VaadinSession.getCurrent().getSession().invalidate();
        // VaadinService.getCurrentRequest().getWrappedSession().invalidate();
        UINavigationHandler.getInstance().navigateToLogoutPage();
    }
}
