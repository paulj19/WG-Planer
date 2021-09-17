package com.wg_planner.views.UnauthorizedPages.intro_page;

import com.vaadin.flow.component.html.Image;

import java.util.ArrayList;
import java.util.List;

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
        List<Image> images = new ArrayList<>();
        for(int i = 1; i <= 7 ; i++ ) {
            Image image = new Image("images/intro/phone/my_tasks_floor_tasks/" + i + ".png ", ".");
            images.add(image);
        }
        illustration.add(new Carousel(getWrappedImages(images)));
    }
}

