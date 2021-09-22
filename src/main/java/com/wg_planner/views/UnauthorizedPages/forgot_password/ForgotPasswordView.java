package com.wg_planner.views.UnauthorizedPages.forgot_password;

import com.vaadin.flow.component.textfield.TextField;
import com.wg_planner.views.UnauthorizedPages.UnauthorizedPagesView;

public class ForgotPasswordView extends UnauthorizedPagesView {
    private TextField username = new TextField("Username", "Enter your username");
    private TextField oldPassword = new TextField("Old Password", "Enter your old");

}
