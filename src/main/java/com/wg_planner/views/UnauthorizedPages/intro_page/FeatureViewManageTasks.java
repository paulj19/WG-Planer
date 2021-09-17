package com.wg_planner.views.UnauthorizedPages.intro_page;

import com.vaadin.flow.component.html.Image;

import java.util.ArrayList;
import java.util.List;

public class FeatureViewManageTasks extends FeatureView {

    public FeatureViewManageTasks() {
        super();
    }

    @Override
    void addTitle() {
        title.add("Manage Tasks");
    }

    @Override
    void addFeatureDescription() {
        description.add("New tasks can be created anytime. All available residents will be notified about it and all have to agree. The task can then be assigned " +
                "to a resident. Tasks can also be later reset to another resident. Deleting tasks works similar to task create where notification will be " +
                "sent to all available residents and they all have to accept.");
    }

    @Override
    void addFeatureIllustration() {
        List<Image> images = new ArrayList<>();
        for(int i = 1; i <= 17 ; i++ ) {
            Image image = new Image("images/intro/phone/manage_tasks/" + i + ".png ", ".");
            images.add(image);
        }
        illustration.add(new Carousel(images));
    }
}