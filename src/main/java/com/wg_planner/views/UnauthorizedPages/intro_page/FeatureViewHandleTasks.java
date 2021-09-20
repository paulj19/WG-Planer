package com.wg_planner.views.UnauthorizedPages.intro_page;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.wg_planner.views.utils.UINavigationHandler;

import java.util.ArrayList;
import java.util.List;

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
        Button registerButton = new Button("Register");
        registerButton.addClassName("register-create-floor-button");
//        registerButton.getStyle().set("width", "15vw");
        registerButton.addClickListener(event -> UINavigationHandler.getInstance().navigateToRegisterPage());
        Button createFloorButton = new Button("Create Floor");
        createFloorButton.addClassName("register-create-floor-button");
//        createFloorButton.getStyle().set("width", "15vw");
        createFloorButton.addClickListener(event -> UINavigationHandler.getInstance().navigateToCreateFloorPage());

        description.getElement().setProperty("innerHTML", "WG Planner helps you better organize your shared apartment. <br/>No more Task Boards hanging " +
                "around! </br> No more undone tasks! <br/>No more disputes! <br/>No more worries when going away!");
        description.setClassName("front-view");


        // description.add("WG Planner helps you better organize your shared apartment. No more Task Boards hanging around! No more undone tasks! No
        // more disputes! No more worries when going away!");
        VerticalLayout buttonLayout = new VerticalLayout(createFloorButton, registerButton);
//        buttonLayout.getStyle().set("align-items", "center");
//        buttonLayout.getStyle().set("margin-top", "10vh");
//        buttonLayout.getStyle().set("margin-left", "10vw");
        buttonLayout.addClassName("button-layout");
        featureBlockText.add(buttonLayout);
        //        featureBlockText.add(createFloorButton);
        featureBlockText.getStyle().set("height", "100vh");
    }

    @Override
    void addFeatureIllustration() {
        List<Image> images = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            Image image = new Image("images/intro/phone/handle_tasks/" + i + ".png ", ".");
            images.add(image);
        }
        illustration.add(new Carousel(images));
    }
}



