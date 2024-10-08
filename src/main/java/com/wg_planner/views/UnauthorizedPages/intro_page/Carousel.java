package com.wg_planner.views.UnauthorizedPages.intro_page;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.*;
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
    Anchor previousBubble = new Anchor();
    Anchor nextBubble = new Anchor();
    HorizontalLayout next = new HorizontalLayout();
    Span rightArrow = new Span();
    Span leftArrow = new Span();
    List<Div> imageList = new ArrayList<>();
    List<Span> dotList = new ArrayList<>();
    HashMap<Span, Div> imageMap = new HashMap<>();
    Div currentlyDisplayed;
    Span currentDot;

    public Carousel(List<Image> images) {
        addClassName("carousel-layout");
        dots.addClassName("dots");
        navigation.addClassNames("navigation");
        rightArrow.addClassNames("arrow", "right");
        leftArrow.addClassNames("arrow", "left");
        Label labelPrevious = new Label("previous");
        labelPrevious.addClassName("label-previous");
        previousBubble.addClassName("previous");
        previousBubble.getElement().setProperty("innerHTML","&laquo; Previous");
        previous.add(previousBubble);
        previous.addClickListener(event -> {
            removeAll();
            currentlyDisplayed = imageList.get((imageList.indexOf(currentlyDisplayed) == 0 ? imageList.size() - 1 : imageList.indexOf(currentlyDisplayed) - 1));
            currentDot.getStyle().set("background-color", "#bbbbbb");
            currentDot = dotList.get((dotList.indexOf(currentDot) == 0 ? dotList.size() - 1 : dotList.indexOf(currentDot) - 1));
            currentDot.getStyle().set("background-color", "#717171");
            add(currentlyDisplayed);
            add(navigation);
        });
        Label labelNext = new Label("next");
        labelNext.addClassName("label-next");
        nextBubble.addClassName("next");
        nextBubble.getElement().setProperty("innerHTML","Next &raquo;");
        next.add(nextBubble);
        next.addClickListener(event -> {
            removeAll();
            currentlyDisplayed = imageList.get((imageList.indexOf(currentlyDisplayed) + 1) % imageList.size());
            currentDot.getStyle().set("background-color", "#bbbbbb");
            currentDot = dotList.get((dotList.indexOf(currentDot) + 1) % dotList.size());
            currentDot.getStyle().set("background-color", "#717171");

            add(currentlyDisplayed);
            add(navigation);
        });
        for (Image image : images) {
            Div div = new Div();
            Span dot = new Span();
            image.addClassName("feature-image");
            dot.addClickListener(event -> {
                removeAll();
                currentlyDisplayed = imageMap.get(dot);
                dot.getStyle().set("background-color", "#717171");
                currentDot.getStyle().set("background-color", "#bbbbbb");
                currentDot = dot;
                add(currentlyDisplayed);
                add(navigation);
            });
            div.add(image);
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
            dotList.add(dot);
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
