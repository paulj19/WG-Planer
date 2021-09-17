package com.wg_planner.views.UnauthorizedPages.intro_page;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Image;

import java.util.ArrayList;
import java.util.List;

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
        List<Image> images = new ArrayList<>();
        Image image = new Image("images/intro/phone/register_floor/1.png", "availability status");
        images.add(image);

        illustration.add(new Carousel(getWrappedImages(images)));
    }
}
