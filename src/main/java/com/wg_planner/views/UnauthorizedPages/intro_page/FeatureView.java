package com.wg_planner.views.UnauthorizedPages.intro_page;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.util.ArrayList;
import java.util.List;

@CssImport("./styles/views/intro/intro.css")
public abstract class FeatureView extends VerticalLayout {
    Div featureBlockWrapper = new Div();
    Div featureBlock = new Div();
    Span description = new Span();
    H1 title = new H1();
    Div illustration = new Div();
    Image phone_frame = new Image("images/iphone_layout.jpg", ".");

    public FeatureView() {
        addClassName("ver-layout");
        featureBlockWrapper.addClassName("feature-block-wrapper");
        featureBlock.addClassName("feature-block");
        title.addClassName("feature-title");
        description.addClassName("feature-description");
        illustration.addClassName("feature-illustration");
        addTitle();
        addFeatureDescription();
        addFeatureIllustration();
        featureBlock.add(illustration, description);
        featureBlockWrapper.add(title, featureBlock);
        add(featureBlockWrapper);
    }

    abstract void addTitle();

    abstract void addFeatureDescription();

    abstract void addFeatureIllustration();

    public List<Component> getWrappedImages(List<Image> images) {
        List<Component> wrappedImages = new ArrayList<>();
        phone_frame.addClassName("phone-frame");
        for (Image image : images) {
            Div phone_frame_wrapper = new Div();
            Div image_wrapper = new Div();
            phone_frame_wrapper.addClassName("phone-frame-wrapper");
            image_wrapper.addClassName("feature-image-wrapper");
            image.addClassName("feature-image");
            image_wrapper.add(image);
            phone_frame_wrapper.add(phone_frame, image_wrapper);
            wrappedImages.add(phone_frame_wrapper);
        }
        return wrappedImages;
    }
}
