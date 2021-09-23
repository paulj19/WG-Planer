package com.wg_planner.views.UnauthorizedPages.intro_page;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Image;

import java.util.ArrayList;
import java.util.List;

@CssImport("./styles/views/intro/intro.css")
public class FeatureViewRegisterResident extends FeatureView {

    public FeatureViewRegisterResident() {
        super();
    }

    @Override
    void addTitle() {
        title.add("Register New Resident");
    }

    @Override
    void addFeatureDescription() {
        description.add("Other residents can register by giving floor code of the floor. The App then asks to select one of the free rooms available in " +
                "the shared apartment. Upon selecting a room, the app generates a one time code. Another registered resident can use this pin to admit the " +
                "new resident into the shared apartment. The images on the left provides a clear guidance to this procedure.");
    }

    @Override
    void addFeatureIllustration() {
        List<Image> images = new ArrayList<>();
        for(int i = 1; i <= 9 ; i++ ) {
            Image image = new Image("images/intro/phone/register_new_resident/" + i + ".png ", ".");
            images.add(image);
        }
        illustration.add(new Carousel(images));
    }
}
