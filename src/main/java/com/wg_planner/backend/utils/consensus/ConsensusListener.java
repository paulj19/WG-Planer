package com.wg_planner.backend.utils.consensus;

public interface ConsensusListener {
    void onAccept(Long consensusObjectId, String notificationId);
    void onReject(Long consensusObjectId);
}
