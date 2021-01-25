package com.wg_planner.views.ResetTask;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;


public class ResetTaskPage extends VerticalLayout {
    H1 heading = new H1("Assign Biomüll to another roommate");

    public ResetTaskPage() {
        add(heading);
    }
}
