package com.wg_planner.views.UnauthorizedPages.intro_page;

import com.vaadin.flow.component.html.Image;

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
        Image image = new Image("images/getting_started/phone/6availability_status.png", "availability status");
        image.addClassName("feature-image");
        illustration.add(new Carousel(image));
    }
}