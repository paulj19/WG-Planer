package com.wg_planner.views.UnauthorizedPages.intro_page;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.wg_planner.views.utils.UINavigationHandler;

import java.util.ArrayList;
import java.util.List;

@CssImport("./styles/views/intro/intro.css")
public class FeatureViewHandleTasks extends FeatureView {

    public FeatureViewHandleTasks() {
        super();
    }

    @Override
    void addTitle() {
        title.add("Flat Sharing Made Easy!");
    }

    @Override
    void addFeatureDescription() {
        VerticalLayout layout = new VerticalLayout();
        Button registerButton = new Button("Register");
        Span check  = new Span();
        check.addClassName("check");
        registerButton.addClassName("register-create-floor-button");
        registerButton.addClickListener(event -> UINavigationHandler.getInstance().navigateToRegisterPage());
        Button createFloorButton = new Button("Create Floor");
        createFloorButton.addClassName("register-create-floor-button");
        createFloorButton.addClickListener(event -> UINavigationHandler.getInstance().navigateToCreateFloorPage());
        HorizontalLayout h = new HorizontalLayout(check, new Span("Tasks done always on time!"));
        layout.add(new Span("WG Planner helps you better organize your shared apartment. The app takes care of the household task management in your shared " +
                "apartment while you enjoy living together."));
        layout.add(new HorizontalLayout(check, new Span("No more Task Boards hanging around!")));
        layout.add(h);
        layout.add(new HorizontalLayout(check, new Span("No more disputes!")));
        description.getElement().setProperty("innerHTML", "WG Planner helps you better organize your shared apartment. The app takes care of the household " +
                "task management in your shared apartment while you enjoy living together. <br/><div class = \"check\"></div>No more Task Boards " +
                "hanging around! </br><div class = \"check\"></div> Tasks done on time! <br/><div class = \"check\"></div>No more disputes!");
        description.setClassName("front-view");
//        description.add(layout);
        VerticalLayout buttonLayout = new VerticalLayout(createFloorButton, registerButton);
        buttonLayout.addClassName("button-layout");
        featureBlockText.add(buttonLayout);
        featureBlockText.getStyle().set("height", "100vh");
    }

    @Override
    void addFeatureIllustration() {
        List<Image> images = new ArrayList<>();
        for (int i = 1; i <= 7; i++) {
            Image image = new Image("images/intro/phone/handle_tasks/" + i + ".png ", ".");
            images.add(image);
        }
        illustration.add(new Carousel(images));
    }
}



