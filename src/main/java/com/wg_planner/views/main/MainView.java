package com.wg_planner.views.main;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.component.page.Viewport;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.RouteConfiguration;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
import com.wg_planner.views.home_page.HomePageView;
import com.wg_planner.views.sub_menu.account_details.AccountDetailsView;
import com.wg_planner.views.sub_menu.account_details.ResidentAvailabilityView;
import com.wg_planner.views.sub_menu.floor_details.FloorDetailsView;
import com.wg_planner.views.tasks.floor_tasks.FloorTasksView;
import com.wg_planner.views.tasks.my_tasks.MyTasksView;
import com.wg_planner.views.utils.AccountDetailsHelper;
import com.wg_planner.views.utils.SessionHandler;
import com.wg_planner.views.utils.UIHandler;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;

import java.util.Arrays;
import java.util.Optional;

/**
 * The main view is a top-level placeholder for other views.
 */
@Push
@JsModule("./styles/shared-styles.js")
@PWA(name = "WG_Planner", shortName = "WG_Planner", enableInstallPrompt = false)
@Theme(value = Lumo.class, variant = Lumo.LIGHT)
@CssImport("./styles/views/main/main-view.css")
@Viewport("width=device-width, minimum-scale=1.0, initial-scale=1.0, user-scalable=yes")
public class MainView extends AppLayout {
    private final Tabs menu;
    final int mobileWindowWidth = 480;//px
    final int standardTabletWindowWidth = 768;//px
    AutowireCapableBeanFactory beanFactory;
    MainViewPresenter mainViewPresenter;
    AccountDetailsHelper accountDetailsHelper;

    public MainView(AutowireCapableBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
        getWindowWidth();
        addBrowserWindowResizeListener();
        menu = createMenu();
        addToNavbar(false, createNavContentTitle());
        mainViewPresenter = new MainViewPresenter();
        accountDetailsHelper = new AccountDetailsHelper();
        beanFactory.autowireBean(mainViewPresenter);
        beanFactory.autowireBean(accountDetailsHelper);
        AccountDetailsHelper.setAccountDetailsHelper(accountDetailsHelper);
        //todo this should go direct after login
        SessionHandler.saveLoggedInResidentAccount(accountDetailsHelper.getLoggedInResidentAccount());
        mainViewPresenter.init();
    }

    private void addBrowserWindowResizeListener() {
        UI.getCurrent().getPage().addBrowserWindowResizeListener(event -> {
            getWindowWidth();
            UI.getCurrent().getPage().reload();
        });
    }

    private void getWindowWidth() {
        UI.getCurrent().getPage().retrieveExtendedClientDetails(details -> {
            if (details.getWindowInnerWidth() <= standardTabletWindowWidth) {
                menu.setOrientation(Tabs.Orientation.HORIZONTAL);
                addToNavbar(true, createNavContentMenuBar(menu));
            } else {
                menu.setOrientation(Tabs.Orientation.VERTICAL);
                addToDrawer(createNavContentMenuBar(menu));
            }
        });
    }

    private Component createSecondaryMenu() {
        Image image = new Image("images/profile_pic.png", "profile pic");
        image.addClassName("profile-pic");
        image.addClickListener(event -> {
            UIHandler.getInstance().navigateToSubMenu();
        });
        return image;
    }

    private Component[] createSubMenuItems() {
        RouterLink[] links = new RouterLink[]{
                new RouterLink("Account Details", AccountDetailsView.class),
                new RouterLink("Floor Details", FloorDetailsView.class),
                new RouterLink("Availability Status", ResidentAvailabilityView.class),
        };
        return Arrays.stream(links).map(MainView::createTab).toArray(Tab[]::new);
    }

    private Component createNavContentTitle() {
        VerticalLayout layout = new VerticalLayout();
        layout.setSizeFull();
        layout.setPadding(false);
        layout.setSpacing(false);
        layout.setAlignItems(FlexComponent.Alignment.CENTER);
        HorizontalLayout logoLayout = new HorizontalLayout();
        logoLayout.setId("logo");
        logoLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        logoLayout.setWidth("100%");
        logoLayout.add(new Image("images/logo.png", "WG Planner"));
        H1 h1 = new H1("WG Planner");
        h1.setWidthFull();
        logoLayout.add(h1);
        logoLayout.add(createSecondaryMenu());
        layout.add(logoLayout);
        return layout;
    }

    private Component createNavContentMenuBar(Tabs menu) {
        VerticalLayout layout = new VerticalLayout();
        layout.addClassName("navigation-bar");
        layout.add(menu);
        layout.setJustifyContentMode(FlexComponent.JustifyContentMode.AROUND);
        return layout;
    }

    private Tabs createMenu() {
        final Tabs tabs = new Tabs();
        tabs.addClassName("main-tabs-view");
        tabs.add(createMenuItems());
        return tabs;
    }

    private Component[] createMenuItems() {
        RouterLink home = new RouterLink("Home", HomePageView.class);
        home.add(createIcon(VaadinIcon.HOME));
        RouterLink floor_tasks = new RouterLink("Floor Tasks", FloorTasksView.class);
        floor_tasks.add(createIcon(VaadinIcon.BULLETS));
        RouterLink my_tasks = new RouterLink("My Tasks", MyTasksView.class);
        my_tasks.add(createIcon(VaadinIcon.GRID_BIG_O));

        RouterLink[] links = new RouterLink[]{home, floor_tasks, my_tasks};
        Arrays.stream(links).forEach(routerLink -> routerLink.addClassNames("navigation-bar-menu"));
        return Arrays.stream(links).map(MainView::createTab).toArray(Tab[]::new);
    }

    private Icon createIcon(VaadinIcon vaadinIcon) {
        Icon icon = vaadinIcon.create();
        icon.addClassName("navigation-bar-icon");
        return icon;
    }

    private static Tab createTab(Component content) {
        final Tab tab = new Tab();
        tab.add(content);
        return tab;
    }

    @Override
    protected void afterNavigation() {
        super.afterNavigation();
        updateChrome();
    }

    private void updateChrome() {
        getTabWithCurrentRoute().ifPresent(menu::setSelectedTab);
    }

    private Optional<Tab> getTabWithCurrentRoute() {
        String currentRoute = RouteConfiguration.forSessionScope()
                .getUrl(getContent().getClass());
        return menu.getChildren().filter(tab -> hasLink(tab, currentRoute))
                .findFirst().map(Tab.class::cast);
    }

    private boolean hasLink(Component tab, String currentRoute) {
        return tab.getChildren().filter(RouterLink.class::isInstance)
                .map(RouterLink.class::cast).map(RouterLink::getHref)
                .anyMatch(currentRoute::equals);
    }
}
