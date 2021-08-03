package com.wg_planner.views.utils;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.shared.Registration;

@CssImport(value = "./styles/views/utils/confirmation-dialog.css")
public class ConfirmationDialog extends Dialog {
    private static final String defaultHeading = "Confirm";
    private static final String defaultConfirmationDialogText = "Are you sure to complete this action?";
    private static final String defaultConfirmButtonText = "Confirm";
    private static final String defaultCancelButtonText = "Cancel";
    private Div heading;
    private Div confirmDialogText;
    private Button confirmButton;
    private Button rejectButton;
    private Button cancelButton;
    private Object value;

    public static class ConfirmationDialogEvent extends ComponentEvent<ConfirmationDialog> {
        Object objectCorrespondingEvent;

        public ConfirmationDialogEvent(ConfirmationDialog source, Object o) {
            super(source, false);
            this.objectCorrespondingEvent = o;
        }

        public Object getObjectCorrespondingEvent() {
            return objectCorrespondingEvent;
        }

        public static class ConfirmEvent extends ConfirmationDialogEvent {
            public ConfirmEvent(ConfirmationDialog source, Object o) {
                super(source, o);
            }
        }

        public static class RejectEvent extends ConfirmationDialogEvent {
            public RejectEvent(ConfirmationDialog source, Object o) {
                super(source, o);
            }
        }

        public static class CancelEvent extends ConfirmationDialogEvent {
            public CancelEvent(ConfirmationDialog source, Object o) {
                super(source, o);
            }
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }

    public ConfirmationDialog(ComponentEventListener<ConfirmationDialogEvent.ConfirmEvent> confirmEventListener,
                              ComponentEventListener<ConfirmationDialogEvent.CancelEvent> cancelEventListener, Object value) {
        this(defaultHeading, defaultConfirmationDialogText, defaultConfirmButtonText, confirmEventListener,
                defaultCancelButtonText, cancelEventListener, value);
    }


    public ConfirmationDialog(String heading, String confirmationDialogText, String confirmText,
                              ComponentEventListener<ConfirmationDialogEvent.ConfirmEvent> confirmEventListener,
                              String cancelText,
                              ComponentEventListener<ConfirmationDialogEvent.CancelEvent> cancelEventListener,
                              Object value) {
        this(heading, confirmationDialogText, confirmText, confirmEventListener, cancelText, cancelEventListener);
        this.value = value;
    }

    public ConfirmationDialog(String heading, String confirmationDialogText, String confirmText,
                              String cancelText,
                              Object value) {
        this(heading, confirmationDialogText, confirmText, null, cancelText, null);
        this.value = value;
    }

    public ConfirmationDialog(String heading, String confirmationDialogText, String confirmText,
                              ComponentEventListener<ConfirmationDialogEvent.ConfirmEvent> confirmEventListener,
                              String cancelText,
                              ComponentEventListener<ConfirmationDialogEvent.CancelEvent> cancelEventListener) {
        setHeading(heading);
        setConfirmDialogText(confirmationDialogText);
        setButtons(confirmText, cancelText);
        confirmButton.addClickListener(event -> {
            fireEvent(new ConfirmationDialogEvent.ConfirmEvent(this, value));
            this.close();
        });
        cancelButton.addClickListener(event -> {
            fireEvent(new ConfirmationDialogEvent.CancelEvent(this, value));
            this.close();
        });
    }

    private void setButtons(String confirmText, String cancelText) {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.getStyle().set("margin-top", "15px");
        buttonLayout.getStyle().set("justify-content", "flex-start");
        setConfirmButtonText(confirmText);
        setCancelButtonText(cancelText);
        buttonLayout.add(cancelButton, confirmButton);
        add(buttonLayout);
    }

    private void setConfirmButtonText(String confirmText) {
        confirmButton = new Button(confirmText);
        confirmButton.addClassName("dialog-button");
    }

    private void setCancelButtonText(String cancelText) {
        cancelButton = new Button(cancelText);
        cancelButton.addClassName("dialog-button");
    }

    private void setHeading(String headingText) {
        heading = new Div(new Span(headingText));
        heading.getStyle().set("font-weight", "600");
        heading.getStyle().set("font-size", "1.3em");
        heading.getStyle().set("margin-bottom", "10px");
        add(heading);
    }

    private void setConfirmDialogText(String confirmDialogContent) {
        confirmDialogText = new Div(new Span(confirmDialogContent));
        add(confirmDialogText);
    }

    public Object getValue() {
        return value;
    }

    public void open() {
        super.open();
    }

    public void close() {
        super.close();
    }
}
