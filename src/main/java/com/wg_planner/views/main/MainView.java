package com.wg_planner.views.main;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.contextmenu.ContextMenu;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.RouteConfiguration;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
import com.wg_planner.views.home_page.HomePageView;
import com.wg_planner.views.tasks.floor_tasks.FloorTasksView;
import com.wg_planner.views.tasks.my_tasks.MyTasksView;
import com.wg_planner.views.utils.AccountDetailsHelper;
import com.wg_planner.views.utils.SessionHandler;
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
public class MainView extends AppLayout {

    private final Tabs menu;
    private H1 viewTitle;
    AutowireCapableBeanFactory beanFactory;
    MainViewPresenter mainViewPresenter;
    AccountDetailsHelper accountDetailsHelper;

    public MainView(AutowireCapableBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
        setPrimarySection(Section.DRAWER);
        //        addToNavbar(true, createHeaderContent(), createSecondaryMenu());
        menu = createMenu();
        addToNavbar(false, createNavContentTitle());
        addToNavbar(true, createNavContentMenuBar(menu));

        //        addToDrawer(createDrawerContent(menu));
        mainViewPresenter = new MainViewPresenter();
        accountDetailsHelper = new AccountDetailsHelper();
        beanFactory.autowireBean(mainViewPresenter);
        beanFactory.autowireBean(accountDetailsHelper);
        AccountDetailsHelper.setAccountDetailsHelper(accountDetailsHelper);
        //todo this should go direct after login
        SessionHandler.saveLoggedInResidentAccount(accountDetailsHelper.getLoggedInResidentAccount());
        mainViewPresenter.init();
    }

    //todo fix menu click sensitivity
    private Component createSecondaryMenu() {
        HorizontalLayout layout = new HorizontalLayout();
        layout.setId("header");
        //        layout.getThemeList().set("dark", true);
        layout.setSpacing(false);
        layout.setAlignItems(FlexComponent.Alignment.CENTER);
        Image image = new Image("images/user.svg", "Avatar");
//        image.addClassName("secondary-menu-image");
        MenuBar menuBar = new MenuBar();
        MenuItem profileImage = menuBar.addItem("");
//        menuBar.addClassName("secondary-menu-image");
//        image.addClickListener(event -> fireEvent(new<>(menuBar)));

        SubMenu secondaryMenu = profileImage.getSubMenu();
        //        secondaryMenu.addItem("Edit Account");
        secondaryMenu.addItem(new Anchor("account_details", "Account Details"));
        secondaryMenu.addItem(new Anchor("floor_details", "Floor Details"));
        secondaryMenu.addItem(new Anchor("logout", "Log out"));
        layout.add(menuBar);

        return layout;
    }

    private Component createNavContentTitle() {
        VerticalLayout layout = new VerticalLayout();
        layout.setSizeFull();
        layout.setPadding(false);
        layout.setSpacing(false);
//        layout.getThemeList().set("spacing-s", true);
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
        layout.setAlignItems(FlexComponent.Alignment.CENTER);
        layout.setJustifyContentMode(FlexComponent.JustifyContentMode.AROUND);
        return layout;
    }

    private Tabs createMenu() {
        final Tabs tabs = new Tabs();
        tabs.setOrientation(Tabs.Orientation.HORIZONTAL);
        //        tabs.addThemeVariants(TabsVariant.LUMO_MINIMAL);
        //        tabs.setId("tabs");
        tabs.add(createMenuItems());
        return tabs;
    }

    private Component[] createMenuItems() {
        FlexLayout flexLayout = new FlexLayout();
        RouterLink home = new RouterLink("Home", HomePageView.class);
//        home.addClassName("navigation-bar");
        home.add(createIcon(VaadinIcon.HOME));
//        home.addClassName("navigation-bar-label");
        RouterLink floor_tasks = new RouterLink("Floor Tasks", FloorTasksView.class);
        floor_tasks.add(createIcon(VaadinIcon.BULLETS));
        RouterLink my_tasks = new RouterLink("My Tasks", MyTasksView.class);
        my_tasks.add(createIcon(VaadinIcon.GRID_BIG_O));
        //        RouterLink admit_resident = new RouterLink("Admit Resident", AdmitNewResidentView.class);

        RouterLink[] links = new RouterLink[]{home, floor_tasks, my_tasks};
        Arrays.stream(links).forEach(routerLink -> routerLink.addClassNames("navigation-bar-menu"));
        return Arrays.stream(links).map(MainView::createTab).toArray(Tab[]::new);
    }

    private Icon createIcon(VaadinIcon vaadinIcon) {
        Icon icon = vaadinIcon.create();
        //        icon.setSize("var(--lumo-icon-size-s)");
        //        icon.getStyle().set("margin", "auto");
        icon.addClassName("navigation-bar-icon");
        //        icon.getStyle()
        //                .set("box-sizing", "border-box")
        //                .set("margin-inline-end", "var(--lumo-space-m)")
        //                .set("padding", "var(--lumo-space-xs)")
        //                .set("vertical-position", "absolute")
        //                .set("top", "0")
        //                .set("bottom", "0")
        //                .set("margin", "auto");
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
        //        viewTitle.setText(getCurrentPageTitle());
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

    private String getCurrentPageTitle() {
        return getContent().getClass().getAnnotation(PageTitle.class).value();
    }
}
