package com.wg_planner.views.UnauthorizedPages.intro_page;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.wg_planner.views.UnauthorizedPages.UnauthorizedPagesViewWithLogin;


@Route(value = "logout", layout = UnauthorizedPagesViewWithLogin.class)
@PageTitle("WG Planer")
public class LogoutView extends VerticalLayout {
    public LogoutView() {
    }
}
