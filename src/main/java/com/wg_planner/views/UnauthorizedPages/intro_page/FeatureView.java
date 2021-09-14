package com.wg_planner.views.UnauthorizedPages.intro_page;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public abstract class FeatureView extends VerticalLayout {
    Div featureBlockWrapper = new Div();
    Div featureBlock = new Div();
    Span description = new Span();
    H1 title = new H1();
    Span illustration = new Span();

    public FeatureView() {
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
}
