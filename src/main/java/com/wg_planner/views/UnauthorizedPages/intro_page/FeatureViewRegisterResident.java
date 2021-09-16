package com.wg_planner.views.UnauthorizedPages.intro_page;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Image;

@CssImport("./styles/views/intro/intro.css")
public class FeatureViewRegisterResident extends FeatureView {

    public FeatureViewRegisterResident() {
        super();
    }

    @Override
    void addTitle() {
        title.add("Register Resident");
    }

    @Override
    void addFeatureDescription() {
        description.add("You can start using WG Planner for your shared apartment by creating a new floor.");
    }

    @Override
    void addFeatureIllustration() {

        Image createFloorScreenShot = new Image("images/getting_stared/phone/7register_floor.png", "create floor screenshot");
        createFloorScreenShot.addClassName("feature-image");
        illustration.add(createFloorScreenShot);
    }
}
