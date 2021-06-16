package com.wg_planner.views.register.admission;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.wg_planner.backend.utils.code_generator.custom_code_generator.CustomCodeCreator;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;

@Route(value = "register")
@PageTitle("Register | WG Planner")
@CssImport("./styles/views/register/register-view.css")
public class NewResidentAdmissionView extends VerticalLayout {
    NewResidentAdmissionPresenter newResidentAdmissionPresenter;
    AutowireCapableBeanFactory beanFactory;
    TextField floorCode = new TextField("Floor Code", "Enter your floor code");
    Button submitButton = new Button("Submit");

    public NewResidentAdmissionView(AutowireCapableBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
        newResidentAdmissionPresenter = new NewResidentAdmissionPresenter();
        beanFactory.autowireBean(newResidentAdmissionPresenter);
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        add(getFloorCodeLayout());
        newResidentAdmissionPresenter.init(this);
    }

    private VerticalLayout getFloorCodeLayout() {
        VerticalLayout floorCodeLayout = new VerticalLayout();
        floorCode.setMaxLength(CustomCodeCreator.CodeGenerationPurposes.ADMISSION_CODE.getCodeLength());
        floorCode.setMinLength(CustomCodeCreator.CodeGenerationPurposes.ADMISSION_CODE.getCodeLength());
        floorCode.setAutofocus(true);
        floorCode.setRequired(true);
        floorCodeLayout.setAlignItems(Alignment.CENTER);
        floorCodeLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        floorCodeLayout.add(floorCode);
        floorCodeLayout.add(submitButton);
//        floorCodeLayout.setMinWidth("250px");
        return floorCodeLayout;
    }
}
