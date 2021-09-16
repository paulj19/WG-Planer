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
        description.add("You can start using WG Planner for your shared apartment by creating a new floor. The creator of the floor can register his/her room" +
                " directly.");
    }

    @Override
    void addFeatureIllustration() {
        Image createFloorScreenShot = new Image("images/getting_stared/phone/7register_floor.png", "create floor screenshot");
        Image registerResidentScreenShot = new Image("images/getting_stared/phone/8register_room.png", "register room screenshot");
        Carousel carousel = new Carousel(createFloorScreenShot, registerResidentScreenShot);
        createFloorScreenShot.addClassName("feature-image");
        registerResidentScreenShot.addClassName("feature-image");
        illustration.add(carousel);
    }
}
