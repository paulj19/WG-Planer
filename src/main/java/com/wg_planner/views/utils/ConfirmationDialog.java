package com.wg_planner.views.utils;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.shared.Registration;

public class ConfirmationDialog extends VerticalLayout {
    private static final String defaultHeading = "Confirm Delete";
    private static final String defaultConfirmationDialogText = "Are you sure to complete this action";
    private static final String defaultConfirmButtonText = "Confirm";
    private static final String defaultCancelButtonText = "Cancel";
    private Div heading;
    private Div confirmDialogText;
    private Button confirmButton;
    private Button rejectButton;
    private Button cancelButton;
    boolean opened = false;
    private Object value;

    public static class ConfirmationDialogEvent extends ComponentEvent<ConfirmationDialog> {
        Object objectCorrespondingEvent;

        public ConfirmationDialogEvent(ConfirmationDialog source, Object o) {
            super(source, false);
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

    public ConfirmationDialog(ComponentEventListener<ConfirmationDialogEvent.ConfirmEvent> confirmEventListener,
                              ComponentEventListener<ConfirmationDialogEvent.CancelEvent> cancelEventListener) {
        this(defaultHeading, defaultConfirmationDialogText, defaultConfirmButtonText, confirmEventListener,
                defaultCancelButtonText, cancelEventListener);
    }

    public ConfirmationDialog(String heading, String confirmationDialogText, String confirmText,
                              ComponentEventListener<ConfirmationDialogEvent.ConfirmEvent> confirmEventListener,
                              String cancelText,
                              ComponentEventListener<ConfirmationDialogEvent.CancelEvent> cancelEventListener) {
        setHeading(heading);
        setConfirmDialogText(confirmationDialogText);
        setButtons(confirmText, cancelText);

        addConfirmListener(confirmEventListener);
        addCancelListener(cancelEventListener);
    }

    public ConfirmationDialog(String heading, String confirmationDialogText, String confirmText,
                              ComponentEventListener<ConfirmationDialogEvent.ConfirmEvent> confirmEventListener,
                              String cancelText,
                              ComponentEventListener<ConfirmationDialogEvent.CancelEvent> cancelEventListener,
                              Object value) {
        this(heading, confirmationDialogText, confirmText, confirmEventListener, cancelText, cancelEventListener);
        this.value = value;
    }

    private void setButtons(String confirmText, String cancelText) {
        setConfirmButtonText(confirmText);
        setCancelButtonText(cancelText);
        add(new HorizontalLayout(confirmButton, cancelButton));
    }

    private void setConfirmButtonText(String confirmText) {
        confirmButton = new Button(confirmText);
    }

    private void setCancelButtonText(String cancelText) {
        cancelButton = new Button(cancelText);
    }

    private void setHeading(String headingText) {
        heading = new Div(new Span(headingText));
        add(heading);
    }

    private void setConfirmDialogText(String confirmDialogContent) {
        confirmDialogText = new Div(new Span(confirmDialogContent));
        add(confirmDialogText);
    }

    private void addConfirmListener(ComponentEventListener<ConfirmationDialogEvent.ConfirmEvent> confirmEventListener) {
        ComponentUtil.addListener(confirmButton, ConfirmationDialogEvent.ConfirmEvent.class, confirmEventListener);
    }

    private void addCancelListener(ComponentEventListener<ConfirmationDialogEvent.CancelEvent> cancelEventListener) {
        ComponentUtil.addListener(cancelButton, ConfirmationDialogEvent.CancelEvent.class, cancelEventListener);
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }

    public Object getValue() {
        return value;
    }

    public void open() {
        if (!opened) {
            UI ui = UI.getCurrent();
            ui.beforeClientResponse(ui, context -> {
                if (getElement().getNode().getParent() == null) {
                    ui.add(this);
                }
            });
            opened = true;
        }
    }

    public void close() {
        if (opened) {
            UI.getCurrent().remove(this);
        }
    }

}
