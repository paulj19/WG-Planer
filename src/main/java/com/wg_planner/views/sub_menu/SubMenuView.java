package com.wg_planner.views.sub_menu;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.wg_planner.views.main.MainView;
import com.wg_planner.views.sub_menu.account_details.AccountDetailsView;
import com.wg_planner.views.sub_menu.account_details.ResidentAvailabilityView;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;

//todo block direct access to this url
@Route(value = "user_details", layout = MainView.class)
@RouteAlias(value = "user_details", layout = MainView.class)
@CssImport(value = "./styles/views/sub-menu/submenu-view.css")
public class SubMenuView extends VerticalLayout {
    private final Tabs subMenu = new Tabs();
    private AutowireCapableBeanFactory beanFactory;

    public SubMenuView(AutowireCapableBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
        addClassName("submenu-layout");
        subMenu.addClassName("submenu-navbar");
        add(subMenu);
        createSubMenuTabs();
    }

    private void createSubMenuTabs() {
        subMenu.setOrientation(Tabs.Orientation.HORIZONTAL);
        Tab accountDetailsTab = addAccountDetailsTab();
        subMenu.add(accountDetailsTab);
        subMenu.add(addAvailabilityStatusTab());
        subMenu.addSelectedChangeListener(event -> {
            if (event.getSelectedTab().hasClassName(AccountDetailsView.class.getName())) {
                removeAll();
                add(subMenu);
                add(new AccountDetailsView(beanFactory));
            } else if (event.getSelectedTab().hasClassName(ResidentAvailabilityView.class.getName())) {
                removeAll();
                add(subMenu);
                add(new ResidentAvailabilityView(beanFactory));
            }
        });
        subMenu.setSelectedTab(accountDetailsTab);
        add(new AccountDetailsView(beanFactory));
    }

    private Tab addAccountDetailsTab() {
        Tab tab = new Tab();
        setTabStyle(tab);
        tab.setClassName(AccountDetailsView.class.getName());
        tab.setLabel("Account Details");
        return tab;
    }

    private Tab addAvailabilityStatusTab() {
        Tab tab = new Tab();
        setTabStyle(tab);
        tab.setClassName(ResidentAvailabilityView.class.getName());
        tab.setLabel("Availability Status");
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
