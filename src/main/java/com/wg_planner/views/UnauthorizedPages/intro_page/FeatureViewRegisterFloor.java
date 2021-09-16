package com.wg_planner.views.UnauthorizedPages.intro_page;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Image;

@CssImport("./styles/views/intro/intro.css")
public class FeatureViewRegisterFloor extends FeatureView {

    public FeatureViewRegisterFloor() {
        super();
    }

    @Override
    void addTitle() {
        title.add("Create Floor");
    }

    @Override
    void addFeatureDescription() {
        description.add("Start using WG Planner for your shared apartment by creating a new floor.");
    }

    @Override
    void addFeatureIllustration() {
        Image image1 = new Image("images/getting_started/phone/7register_floor.png", "create floor");
        Image image2 = new Image("images/getting_started/phone/8register_room.png", "register room");
        Carousel carousel = new Carousel(image1, image2);
        image1.addClassName("feature-image");
        image2.addClassName("feature-image");
        illustration.add(carousel);
    }
}
