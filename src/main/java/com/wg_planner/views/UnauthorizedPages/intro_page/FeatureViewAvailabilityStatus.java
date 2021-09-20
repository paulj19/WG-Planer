package com.wg_planner.views.UnauthorizedPages.intro_page;

import com.vaadin.flow.component.html.Image;

import java.util.ArrayList;
import java.util.List;

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
        description.getElement().setProperty("innerHTML","Got out in a rush and forgot to tell your neighbors that you are away, worried about the tasks assigned to you? Are you sick to " +
                "complete tasks?</br> Availability status setter takes care of this. Change your Availability Status anytime and tasks assigned to " +
                "you will be transferred to next available resident. No more tasks will be assigned to you until you change the status back.");
    }

    @Override
    void addFeatureIllustration() {
        List<Image> images = new ArrayList<>();
        Image image = new Image("images/intro/phone/handle_tasks/1.png ", ".");
        images.add(image);
        illustration.add(new Carousel(images));
    }
}
