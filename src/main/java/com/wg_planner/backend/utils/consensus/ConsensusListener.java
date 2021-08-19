package com.wg_planner.backend.utils.consensus;

public interface ConsensusListener {
    void onAccept(Object objectForConsensus, String notificationId);
    void onReject(Object objectForConsensus, String notificationId);
}
