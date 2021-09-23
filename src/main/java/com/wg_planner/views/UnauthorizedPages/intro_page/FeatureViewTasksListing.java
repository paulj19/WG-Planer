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
        description.add("Floor Tasks lists all tasks in a shared apartment. Each task box contains a task name, assigned room and appropriate " +
                "action corresponding to the task and My Tasks lists all tasks assigned to the logged in resident.");
    }

    @Override
    void addFeatureIllustration() {
        List<Image> images = new ArrayList<>();
        for (int i = 1; i <= 7; i++) {
            Image image = new Image("images/intro/phone/my_tasks_floor_tasks/" + i + ".png ", ".");
            images.add(image);
        }
        illustration.add(new Carousel(images));
    }
}

