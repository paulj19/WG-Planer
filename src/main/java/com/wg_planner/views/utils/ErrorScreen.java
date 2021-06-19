package com.wg_planner.views.utils;

import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class ErrorScreen extends VerticalLayout {
    private static String defaultErrorMessage = "An error has occurred, please try again";

    public ErrorScreen() {
        add(new Span(defaultErrorMessage));
    }

    public ErrorScreen(String customErrorMessage) {
        add(new Span(customErrorMessage));
    }

}
