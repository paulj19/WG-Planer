package com.wg_planner.views.utils.broadcaster;

import com.wg_planner.backend.entity.Room;
import com.wg_planner.views.utils.UINotificationHandler.UINotificationType;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

public class UIMessageBus {


    private static final List<BroadcastListener> listeners = new CopyOnWriteArrayList<>();

    public static void register(BroadcastListener listener) {
        removeDuplicates(listener.getCorrespondingRoom());
        listeners.add(listener);
    }

    private static void removeDuplicates(Room roomToRegister) {
        listeners.removeIf(broadcastListener -> broadcastListener.getCorrespondingRoom().getId().equals(roomToRegister.getId()));
    }

    public static void unregister(BroadcastListener listener) {
        listeners.remove(listener);
    }

    //session handler sessions do not work with receiverBroadcast because it is this session that is calling and not
    // the other view's session
    public static void broadcast(final UINotificationType uiNotificationType) {
        listeners.forEach(listener -> listener.receiveBroadcast(uiNotificationType));
    }

    public static void unicastTo(final UINotificationType uiNotificationType, Room roomToUnicast) {
        Optional<BroadcastListener> NotificationsPageToUnicast =
                listeners.stream().filter(listener -> listener.getCorrespondingRoom().equals(roomToUnicast)).findFirst();
        NotificationsPageToUnicast.ifPresent(listener -> listener.receiveBroadcast(uiNotificationType));
    }

    public interface BroadcastListener {
        void receiveBroadcast(UINotificationType message);

        Room getCorrespondingRoom();
    }
}
