package com.wg_planner.backend.security;

import com.wg_planner.views.UnauthorizedPages.create_floor.CreateFloorView;
import com.wg_planner.views.UnauthorizedPages.intro_page.AppOverviewPage;
import com.wg_planner.views.login.LoginView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinServiceInitListener;
import com.wg_planner.views.UnauthorizedPages.register.RegisterView;
import com.wg_planner.views.UnauthorizedPages.register.admission.NewResidentAdmissionView;
import org.springframework.stereotype.Component;

@Component
public class ConfigureUIServiceInitListener implements VaadinServiceInitListener {
    @Override
    public void serviceInit(ServiceInitEvent event) {
        event.getSource().addUIInitListener(uiEvent -> {
            final UI ui = uiEvent.getUI();
            ui.addBeforeEnterListener(this::authenticateNavigation);
        });
    }
    private void authenticateNavigation(BeforeEnterEvent event) {
        if (!LoginView.class.equals(event.getNavigationTarget())
                && !RegisterView.class.equals(event.getNavigationTarget())
                && !CreateFloorView.class.equals(event.getNavigationTarget())
                && !AppOverviewPage.class.equals(event.getNavigationTarget())
                && !NewResidentAdmissionView.class.equals(event.getNavigationTarget())
                && !SecurityUtils.isUserLoggedIn()) {
            event.rerouteTo(LoginView.class);
        }
    }
}