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
        description.add("New tasks can be created anytime. All available residents will be notified. The task gets created when all agree. The task will then" +
                " be available to be assigned to a resident. Tasks can also be later reassigned to another resident. Just like creating tasks, tasks can also be deleted " +
                "anytime notifications will be sent to all available residents. Once all residents accept, the task gets deleted.");
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