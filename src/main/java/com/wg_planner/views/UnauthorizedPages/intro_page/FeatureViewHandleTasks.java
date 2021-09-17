package com.wg_planner.views.UnauthorizedPages.intro_page;

import com.vaadin.flow.component.html.Image;

import java.util.ArrayList;
import java.util.List;

public class FeatureViewHandleTasks extends FeatureView {

    public FeatureViewHandleTasks() {
        super();
    }

    @Override
    void addTitle() {
        title.add("Handle Tasks");
    }

    @Override
    void addFeatureDescription() {
        description.add("Once you are done with a task, press DONE and the task will be assigned to next available resident. You can also remind someone " +
                "about a task. A push notification will be send to their phone along with a notification in the app.");
    }

    @Override
    void addFeatureIllustration() {
        List<Image> images = new ArrayList<>();
        for(int i = 1; i <= 5 ; i++ ) {
            Image image = new Image("images/intro/phone/handle_tasks/" + i + ".png ", ".");
            images.add(image);
        }
        illustration.add(new Carousel(getWrappedImages(images)));
    }
}



