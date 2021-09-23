package com.wg_planner.views.UnauthorizedPages.intro_page;

import com.vaadin.flow.component.html.Image;

import java.util.ArrayList;
import java.util.List;

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
        description.add("You get notifications about different events in your shared apartment. Other residents can also sent you task " +
                "reminder notifications.");
    }

    @Override
    void addFeatureIllustration() {
        List<Image> images = new ArrayList<>();
        for(int i = 1; i <= 2 ; i++ ) {
            Image image = new Image("images/intro/phone/notifications/" + i + ".png ", ".");
            images.add(image);
        }
        illustration.add(new Carousel(images));
    }
}
