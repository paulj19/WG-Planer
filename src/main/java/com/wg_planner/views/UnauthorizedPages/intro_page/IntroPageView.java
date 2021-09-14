package com.wg_planner.views.UnauthorizedPages.intro_page;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.wg_planner.views.UnauthorizedPages.UnauthorizedPagesView;

@Route(value = "getting_started", layout = UnauthorizedPagesView.class)
@PageTitle("Getting Started | WG Planner")
public class IntroPageView extends VerticalLayout {

    public IntroPageView() {
        addTitle();
        addAppDescription();
        add(new FeatureViewRegisterFloor());
    }

    void addTitle() {
        add(new H1("Getting Started"));
    }

    void addAppDescription() {
        add("WG Planner helps you create and assign tasks to residents in your shared apartment and remind them about it. Once you" +
                " are done with a task, it automatically goes to the next available resident. If you are going away, you can update your status and no tasks " +
                "will be assigned to you until you come back. When you are away all your assigned tasks will be automatically assigned to next available " +
                "resident.");
    }

}