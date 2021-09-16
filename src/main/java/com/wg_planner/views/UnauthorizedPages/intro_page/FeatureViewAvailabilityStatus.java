package com.wg_planner.views.UnauthorizedPages.intro_page;

import com.vaadin.flow.component.html.Image;

public class FeatureViewAvailabilityStatus extends FeatureView {

    public FeatureViewAvailabilityStatus() {
        super();
    }

    @Override
    void addTitle() {
        title.add("Availability Status");
    }

    @Override
    void addFeatureDescription() {
        description.add("Got out in a rush and forgot to tell your neighbors that you are away, worried about the tasks assigned to you? Are you sick to " +
                "complete tasks? Availability status setter takes care of this. Change your Availability Status anytime and tasks assigned to " +
                "you will be transferred to next available resident. No more tasks will be assigned to you until you change the status back.");
    }

    @Override
    void addFeatureIllustration() {
        Image image = new Image("images/getting_started/phone/6availability_status.png", "availability status");
        image.addClassName("feature-image");
        illustration.add(new Carousel(image));
    }
}
