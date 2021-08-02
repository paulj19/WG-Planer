package com.wg_planner.views.floor_something;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.wg_planner.views.main.MainView;
import com.wg_planner.views.sub_menu.floor_details.FloorDetailsView;
import com.wg_planner.views.tasks.floor_tasks.FloorTasksView;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;

@Route(value = "floor_something", layout = MainView.class)
@PageTitle("Floor Something")
@CssImport("./styles/views/floor-something/floor-something-view.css")
public class FloorSomethingView extends VerticalLayout {
    private AutowireCapableBeanFactory beanFactory;
    private final Tabs subMenu = new Tabs();

    public FloorSomethingView(AutowireCapableBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
        addClassName("submenu-layout");
        subMenu.addClassName("submenu-navbar");
        add(subMenu);
        createSubMenuTabs();
    }

    private void createSubMenuTabs() {
        subMenu.setOrientation(Tabs.Orientation.HORIZONTAL);
        Tab floorTasksTab = addFloorTasksTab();
        subMenu.add(floorTasksTab);
        subMenu.add(addFloorDetailsTab());
        subMenu.addSelectedChangeListener(event -> {
            if (event.getSelectedTab().hasClassName(FloorTasksView.class.getName())) {
                removeAll();
                add(subMenu);
                add(new FloorTasksView(beanFactory));
            } else if (event.getSelectedTab().hasClassName(FloorDetailsView.class.getName())) {
                removeAll();
                add(subMenu);
                add(new FloorDetailsView(beanFactory));
            }
        });
        subMenu.setSelectedTab(floorTasksTab);
        add(new FloorTasksView(beanFactory));
    }

    private Tab addFloorTasksTab() {
        Tab tab = new Tab();
        setTabStyle(tab);
        tab.setClassName(FloorTasksView.class.getName());
        tab.setLabel("Floor Tasks");
        return tab;
    }

    private Tab addFloorDetailsTab() {
        Tab tab = new Tab();
        setTabStyle(tab);
        tab.setClassName(FloorDetailsView.class.getName());
        tab.setLabel("Floor Details");
        return tab;
    }

    private void setTabStyle(Tab tab) {
        tab.getStyle().set("box-sizing", "content-box");
        tab.getStyle().set("padding-top", "0px");
        tab.getStyle().set("padding-bottom", "0px");
        tab.getStyle().set("padding-right", "2.4vw");
        tab.getStyle().set("padding-left", "2.4vw");
        tab.getStyle().set("background", "none");

    }
}
