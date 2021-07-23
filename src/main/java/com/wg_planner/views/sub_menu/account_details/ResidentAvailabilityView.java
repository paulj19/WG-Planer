package com.wg_planner.views.sub_menu.account_details;

import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.wg_planner.backend.Service.ResidentAccountService;
import com.wg_planner.views.main.MainView;
import com.wg_planner.views.utils.SessionHandler;
import com.wg_planner.views.utils.UIStringConstants;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;

@Route(value = "resident_availability", layout = MainView.class)
@PageTitle("Resident Availability")
public class ResidentAvailabilityView extends VerticalLayout {
    ResidentAccountService residentAccountService;
    ResidentAvailabilityPresenter residentAvailabilityPresenter;
    private AutowireCapableBeanFactory beanFactory;
    boolean isAway;

    public ResidentAvailabilityView(AutowireCapableBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
        residentAvailabilityPresenter = new ResidentAvailabilityPresenter();
        beanFactory.autowireBean(residentAvailabilityPresenter);
        this.residentAccountService = residentAccountService;
        add(getHeading());
        addAvailabilityCheckBox();
    }

    private void addAvailabilityCheckBox() {
        Checkbox isAwayCheckBox = new Checkbox("I am away");
        isAwayCheckBox.setValue(SessionHandler.getLoggedInResidentAccount().isAway());
        Checkbox isBackCheckBox = new Checkbox("I am ready to take tasks");
        isBackCheckBox.setValue(!SessionHandler.getLoggedInResidentAccount().isAway());
        isAwayCheckBox.addValueChangeListener(event -> {
            isAway = isAwayCheckBox.getValue();
            residentAvailabilityPresenter.setResidentAwayStatusAndSave(isAway);
            isBackCheckBox.setValue(!isAwayCheckBox.getValue());
            Notification.show(UIStringConstants.getInstance().getAvailabilityStatusChanged());
        });
        isBackCheckBox.addValueChangeListener(event -> {
            isAwayCheckBox.setValue(!isBackCheckBox.getValue());
        });
        add(isAwayCheckBox, isBackCheckBox);
    }

    private H1 getHeading() {
        return new H1("Change availability status");
    }
}
