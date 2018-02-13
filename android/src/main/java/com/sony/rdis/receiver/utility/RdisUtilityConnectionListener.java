package com.sony.rdis.receiver.utility;

public interface RdisUtilityConnectionListener {
    void onConnected(RdisUtilityGamePad rdisUtilityGamePad);

    void onDisconnected(RdisUtilityGamePad rdisUtilityGamePad);
}
