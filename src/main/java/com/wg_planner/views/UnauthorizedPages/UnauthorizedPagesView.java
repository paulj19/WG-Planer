package com.wg_planner.views.UnauthorizedPages;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.component.page.Viewport;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
import com.wg_planner.views.main.MainView;

@Push
@JsModule("./styles/shared-styles.js")
@Theme(value = Lumo.class, variant = Lumo.LIGHT)
@Viewport("width=device-width, minimum-scale=1.0, initial-scale=1.0, user-scalable=yes")
public class UnauthorizedPagesView extends AppLayout {
    VerticalLayout layout = new VerticalLayout();
    HorizontalLayout logoLayout = new HorizontalLayout();

    public UnauthorizedPagesView() {
        addToNavbar(createNavContentTitle());
    }

    private Component createNavContentTitle() {
        layout.setSizeFull();
        layout.setPadding(false);
        layout.setSpacing(false);
        layout.setAlignItems(FlexComponent.Alignment.CENTER);
        logoLayout.setId("logo");
        logoLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        logoLayout.setWidth("100%");
        logoLayout.add(MainView.getAppNameHeader());
        layout.add(logoLayout);
        return layout;
    }
}
