package com.wg_planner.views.UnauthorizedPages.intro_page;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@CssImport("./styles/views/intro/carousel.css")
public class Carousel extends VerticalLayout {
    Div dots = new Div();
    HorizontalLayout navigation = new HorizontalLayout();
    HorizontalLayout previous = new HorizontalLayout();
    HorizontalLayout next = new HorizontalLayout();
    Span rightArrow = new Span();
    Span leftArrow = new Span();
    List<Div> imageList = new ArrayList<>();
    HashMap<Span, Div> imageMap = new HashMap<>();
    Div currentlyDisplayed;
    Span currentDot;

    public Carousel(List<Component> components) {
        dots.getStyle().set("text-align", "center")
                .set("width", "83%");
        navigation.getStyle().set("width", "80%").set("padding-left", "10px");
        rightArrow.addClassNames("arrow", "right");
        leftArrow.addClassNames("arrow", "left");
        previous.add(leftArrow, new Label("previous"));
        previous.getStyle().set("align-items", "center");
        previous.addClickListener(event -> {
            removeAll();
            currentlyDisplayed = imageList.get((imageList.indexOf(currentlyDisplayed) + 1) % imageList.size());
            add(currentlyDisplayed);
            add(navigation);
        });
        next.add(new Label("next"), rightArrow);
        next.getStyle().set("align-items", "center");
        next.addClickListener(event -> {
            removeAll();
            currentlyDisplayed = imageList.get((imageList.indexOf(currentlyDisplayed) == 0 ? imageList.size() - 1 : imageList.indexOf(currentlyDisplayed) - 1));
            add(currentlyDisplayed);
            add(navigation);
        });
        for (Component component : components) {
            Div div = new Div();
            Span dot = new Span();
            dot.addClickListener(event -> {
                removeAll();
                currentlyDisplayed = imageMap.get(dot);
                dot.getStyle().set("background-color", "#717171");
                currentDot.getStyle().set("background-color", "#bbbbbb");
                currentDot = dot;
                add(currentlyDisplayed);
                add(navigation);
            });
            div.add(component);

//            div.addClassNames("mySlides", "fade");
//            dot.addClassName("dot");
            setDotProperties(dot);
            if (imageMap.isEmpty()) {
                currentlyDisplayed = div;
                currentDot = dot;
                currentDot.getStyle().set("background-color", "#717171");
                add(currentlyDisplayed);
            }
            imageList.add(div);
            imageMap.put(dot, div);
            dots.add(dot);
        }
        if (imageList.size() > 1) {
            navigation.add(previous, dots, next);
        }
        add(navigation);
    }

    private void setDotProperties(Span dot) {
        dot.getStyle().set("width", "15px").
                set("height", "15px").
                set("background-color", "#bbbbbb").
                set("border-radius", "50%").
                set("display", "inline-block").
                set("cursor", "pointer").
                set("transition", "background-color 0.6s ease").
                set("margin", "0 2px");
    }
}
