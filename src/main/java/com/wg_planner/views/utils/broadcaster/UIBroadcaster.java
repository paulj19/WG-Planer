package com.wg_planner.views.utils.broadcaster;

import com.wg_planner.views.utils.UINotificationHandler.UINotificationType;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class UIBroadcaster {

    private static final List<BroadcastListener> listeners = new CopyOnWriteArrayList<BroadcastListener>();

    public static void register(BroadcastListener listener) {
        listeners.add(listener);
    }

    public static void unregister(BroadcastListener listener) {
        listeners.remove(listener);
    }

    public static void broadcast(final UINotificationType message) {
        listeners.forEach(listener -> listener.receiveBroadcast(message));
    }

    public interface BroadcastListener {
        public void receiveBroadcast(UINotificationType message);
    }
}
