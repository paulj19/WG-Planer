package com.wg_planner.views.UnauthorizedPages.intro_page;

import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Image;

import java.util.ArrayList;
import java.util.List;

public class FeatureViewHandleTasks extends FeatureView {

    public FeatureViewHandleTasks() {
        super();
    }

    @Override
    void addTitle() {
        title.add("Tasks Made Easy!");
    }

    @Override
    void addFeatureDescription() {
        Anchor register = new Anchor("register", "New Resident Register");
        Anchor create_floor = new Anchor("create_floor", "Create Floor");

        description.add("WG Planner helps you better organize your shared apartment. No more Task Boards hanging around! No more undone tasks! No more " +
                "disputes! No more worries when going away!");
        featureBlockText.getStyle().set("height", "100vh");
        description.add(register);
        description.add(create_floor);
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



