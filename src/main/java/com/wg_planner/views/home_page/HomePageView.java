package com.wg_planner.views.home_page;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.wg_planner.backend.utils.LogHandler;
import com.wg_planner.views.main.MainView;
import com.wg_planner.views.sub_menu.floor_details.FloorDetailsView;
import com.wg_planner.views.tasks.floor_tasks.FloorTasksView;
import com.wg_planner.views.utils.SessionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;

@PageTitle("Home")
@Route(value = "home", layout = MainView.class)
@RouteAlias(value = "", layout = MainView.class)
@CssImport("./styles/views/home-page/home-page-view.css")
public class HomePageView extends VerticalLayout {
    private static final Logger LOGGER = LoggerFactory.getLogger(HomePageView.class);
    private AutowireCapableBeanFactory beanFactory;
    private final Tabs subMenu = new Tabs();

    public HomePageView(AutowireCapableBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
        addClassName("submenu-layout");
        subMenu.addClassName("submenu-navbar");
        add(subMenu);
        createSubMenuTabs();
        LOGGER.info(LogHandler.getTestRun(), "Resident Account id {}. Home page view selected.",
                SessionHandler.getLoggedInResidentAccount().getId());
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
