package com.wg_planner.views.UnauthorizedPages.intro_page;

import com.vaadin.flow.component.html.Image;

public class FeatureViewTasksListing extends FeatureView {

    public FeatureViewTasksListing() {
        super();
    }

    @Override
    void addTitle() {
        title.add("My Tasks and Floor Tasks");
    }

    @Override
    void addFeatureDescription() {
        description.add("You can view all tasks in the floor in Floor Tasks and all tasks assigned to you in My Tasks");
    }

    @Override
    void addFeatureIllustration() {
        Image image1 = new Image("images/getting_started/phone/1floor_tasks.png", "my tasks");
        Image image2 = new Image("images/getting_started/phone/2my_tasks.png", "my tasks");
        image1.addClassName("feature-image");
        image2.addClassName("feature-image");
        illustration.add(new Carousel(image1, image2));
    }
}

