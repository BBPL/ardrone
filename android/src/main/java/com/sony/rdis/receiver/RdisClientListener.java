package com.sony.rdis.receiver;

import java.util.List;

public interface RdisClientListener {
    void onStatusChanged(int i, List<RdisClient> list);
}
