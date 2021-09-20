package com.wg_planner.views.UnauthorizedPages.intro_page;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.wg_planner.views.UnauthorizedPages.UnauthorizedPagesView;
import com.wg_planner.views.UnauthorizedPages.UnauthorizedPagesViewWithLogin;
import com.wg_planner.views.utils.UINavigationHandler;

@Route(value = "overview", layout = UnauthorizedPagesViewWithLogin.class)
@PageTitle("WG Planner")
@CssImport("./styles/views/intro/intro.css")
public class AppOverviewPage extends VerticalLayout {

    public AppOverviewPage() {
        addClassName("ver-layout");
        add(new FeatureViewHandleTasks());
        add(new FeatureViewRegisterFloor());
        add(new FeatureViewRegisterResident());
        add(new FeatureViewTasksListing());
        add(new FeatureViewManageTasks());
        add(new FeatureViewAvailabilityStatus());
        add(new FeatureViewNotifications());
    }
}