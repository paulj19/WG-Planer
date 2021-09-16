package com.wg_planner.views.UnauthorizedPages.intro_page;

import com.vaadin.flow.component.html.Image;

public class FeatureViewNotifications extends FeatureView {

    public FeatureViewNotifications() {
        super();
    }

    @Override
    void addTitle() {
        title.add("Notifications");
    }

    @Override
    void addFeatureDescription() {
        description.add("You get notifications about different events in your shared apartment. Your neighbors can also sent you task " +
                "reminder notifications.");
    }

    @Override
    void addFeatureIllustration() {
        Image image = new Image("images/getting_started/phone/3notifications.png", "notifications");
        image.addClassName("feature-image");
        illustration.add(new Carousel(image));
    }
}
