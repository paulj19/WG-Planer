package com.wg_planner.views.UnauthorizedPages.intro_page;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;

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
        description.add("You can start using WG Planner for your shared apartment by creating a new floor.");
    }

    @Override
    void addFeatureIllustration() {
        Image createFloorScreenShot = new Image("images/intro_images/phone/7register_floor.png", "create floor screenshot");
        createFloorScreenShot.addClassName("feature-image");
        illustration.add(createFloorScreenShot);
    }
}
