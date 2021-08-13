package com.wg_planner.views.main;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
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
import com.wg_planner.backend.utils.LogHandler;
import com.wg_planner.views.home_page.HomePageView;
import com.wg_planner.views.notifications_page.NotificationsPageView;
import com.wg_planner.views.sub_menu.account_details.AccountDetailsView;
import com.wg_planner.views.sub_menu.account_details.ResidentAvailabilityView;
import com.wg_planner.views.sub_menu.floor_details.FloorDetailsView;
import com.wg_planner.views.tasks.my_tasks.MyTasksView;
import com.wg_planner.views.utils.AccountDetailsHelper;
import com.wg_planner.views.utils.SessionHandler;
import com.wg_planner.views.utils.UINavigationHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;

import java.util.Arrays;
import java.util.Optional;

/**
 * The main view is a top-level placeholder for other views.
 */
@Push
@JsModule("@vaadin/vaadin-lumo-styles/badge.js")
@JsModule("./styles/shared-styles.js")
@PWA(name = "WG_Planner", shortName = "WG_Planner", enableInstallPrompt = false)
@Theme(value = Lumo.class, variant = Lumo.LIGHT)
@CssImport(value = "./styles/views/main/main-view.css", include = "lumo-badge")
@Viewport("width=device-width, minimum-scale=1.0, initial-scale=1.0, user-scalable=yes")
public class MainView extends AppLayout {
    private static final Logger LOGGER = LoggerFactory.getLogger(MainView.class);
    private final Tabs menu;
    final int mobileWindowWidth = 480;//px
    final int standardTabletWindowWidth = 768;//px
    AutowireCapableBeanFactory beanFactory;
    MainViewPresenter mainViewPresenter;
    AccountDetailsHelper accountDetailsHelper;

    public MainView(AutowireCapableBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
        mainViewPresenter = new MainViewPresenter();
        accountDetailsHelper = new AccountDetailsHelper();
        beanFactory.autowireBean(mainViewPresenter);
        beanFactory.autowireBean(accountDetailsHelper);
        AccountDetailsHelper.setAccountDetailsHelper(accountDetailsHelper);
        SessionHandler.saveLoggedInResidentAccount(accountDetailsHelper.getLoggedInResidentAccount());
        getWindowWidth();
        addBrowserWindowResizeListener();
        menu = createMenu();
        addToNavbar(false, createNavContentTitle());
        LOGGER.info(LogHandler.getTestRun(), "Resident Account id {}. Main page view selected.", SessionHandler.getLoggedInResidentAccount().getId());
        //todo this should go direct after login
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
                LOGGER.info(LogHandler.getTestRun(), "Resident Account id {}. Screen size changed to {}.",
                        details.getWindowInnerWidth(), SessionHandler.getLoggedInResidentAccount().getId());
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
            UINavigationHandler.getInstance().navigateToSubMenu();
        });
        return image;
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

        logoLayout.add(getAppNameHeader());
        logoLayout.add(createSecondaryMenu());
        layout.add(logoLayout);
        return layout;
    }

    public static Div getAppNameHeader() {
        Div logo = new Div();
        logo.setText("WG Planner");
        logo.addClassName("logo-wg-part");
        return logo;
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
        RouterLink my_tasks = new RouterLink("My Tasks", MyTasksView.class);
        my_tasks.add(createIcon(VaadinIcon.BULLETS));
        RouterLink notifications = new RouterLink("Notifications", NotificationsPageView.class);
        getNotificationIconWithBadge(notifications);
        RouterLink[] links = new RouterLink[]{home, my_tasks, notifications};
        Arrays.stream(links).forEach(routerLink -> routerLink.addClassNames("navigation-bar-menu"));
        return Arrays.stream(links).map(MainView::createTab).toArray(Tab[]::new);
    }

    private Span getNotificationIconWithBadge(RouterLink notifications) {
        Icon notificationIcon = createIcon(VaadinIcon.BELL_O);
        Span notSpan = new Span(notificationIcon);
        int numberOfExistingNotifications = mainViewPresenter.getNumberOfNotificationsOfResident();
        if (numberOfExistingNotifications != 0) {
            Span numberOfNotifications = new Span(String.valueOf(numberOfExistingNotifications));
            numberOfNotifications.getElement()
                    .getThemeList()
                    .addAll(Arrays.asList("badge", "error", "primary", "small", "pill"));
            numberOfNotifications.getStyle()
                    .set("position", "absolute")
                    .set("transform", "translate(80%, -60%)")
                    .set("width", "0px")
                    .set("height", "18px");
            notifications.getElement().appendChild(numberOfNotifications.getElement());
            notifications.addBlurListener(event -> {
                if (notifications.getElement().getChildren().anyMatch(element -> element.equals(numberOfNotifications.getElement()))) {
                    notifications.getElement().removeChild(numberOfNotifications.getElement());
                }
            });
        }
        notifications.add(notSpan);
        return notSpan;
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
