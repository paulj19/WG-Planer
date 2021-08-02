package com.wg_planner.views.UnauthorizedPages;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Viewport;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;

@JsModule("./styles/shared-styles.js")
@Theme(value = Lumo.class, variant = Lumo.LIGHT)
@Viewport("width=device-width, minimum-scale=1.0, initial-scale=1.0, user-scalable=yes")
public class UnauthorizedPagesView extends AppLayout {

    public UnauthorizedPagesView() {
        addToNavbar(createNavContentTitle());
    }

    private Component createNavContentTitle() {
        VerticalLayout layout = new VerticalLayout();
        layout.setSizeFull();
        layout.setPadding(false);
        layout.setSpacing(false);
        layout.setAlignItems(FlexComponent.Alignment.CENTER);
        HorizontalLayout logoLayout = new HorizontalLayout();
        logoLayout.setId("logo");
        logoLayout.add(new Image("images/logo.png", "WG Planner"));
        logoLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        logoLayout.setWidth("100%");
        H1 h1 = new H1("WG Planner");
        h1.setWidthFull();
        logoLayout.add(h1);
        layout.add(logoLayout);
        return layout;
    }
}
