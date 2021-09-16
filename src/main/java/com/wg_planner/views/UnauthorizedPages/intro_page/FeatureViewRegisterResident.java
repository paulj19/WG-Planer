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
        description.add("Other residents can now register by giving floor code of the floor. Upon selecting a room, the app " +
                "generates a one time code. Another registered resident can use this pin to admit the new resident into the floor.");
    }

    @Override
    void addFeatureIllustration() {
        Image image = new Image("images/getting_started/phone/8register_room.png", "register resident");
        image.addClassName("feature-image");
        illustration.add(new Carousel(image));
    }
}
