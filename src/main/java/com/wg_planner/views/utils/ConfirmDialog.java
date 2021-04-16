//package com.wg_planner.views.utils;
//
//import com.vaadin.flow.component.button.Button;
//import com.vaadin.flow.component.dialog.Dialog;
//import com.vaadin.flow.component.html.Span;
//
//public class ConfirmDialog {
//    Dialog dialog = new Dialog();
//    Dialog dialogx = new Dialog();
//    Dialog x = new Dialog();
//
//    dialog.setCloseOnEsc(false);
//    dialog.setCloseOnOutsideClick(false);
//
//    Span message = new Span();
//
//    Button confirmButton = new Button("Confirm", event -> {
//        message.setText("Confirmed!");
//        dialog.close();
//    });
//    Button cancelButton = new Button("Cancel", event -> {
//        message.setText("Cancelled...");
//        dialog.close();
//    });
//    dialog.add(confirmButton,cancelButton);
//}
