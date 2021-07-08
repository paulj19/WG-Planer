package com.wg_planner.views.utils.broadcaster;

import com.wg_planner.backend.entity.Room;
import com.wg_planner.views.utils.UINotificationHandler.UIEventType;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class UIBroadcaster {


    private static final List<BroadcastListener> listeners = new CopyOnWriteArrayList<>();

    public static void register(BroadcastListener listener) {
        removeDuplicates(listener.getCorrespondingRoom());
        listeners.add(listener);
    }

    private static void removeDuplicates(Room roomToRegister) {
        listeners.removeIf(broadcastListener -> broadcastListener.getCorrespondingRoom().getId() == roomToRegister.getId());
    }

    public static void unregister(BroadcastListener listener) {
        listeners.remove(listener);
    }

    //session handler sessions do not work with receiverBroadcast because it is this session that is calling and not
    // the other view's session
    public static void broadcast(final UIEventType uiNotification) {
        listeners.forEach(listener -> listener.receiveBroadcast(uiNotification));
    }

    public interface BroadcastListener {
        void receiveBroadcast(UIEventType message);

        Room getCorrespondingRoom();
    }
}
