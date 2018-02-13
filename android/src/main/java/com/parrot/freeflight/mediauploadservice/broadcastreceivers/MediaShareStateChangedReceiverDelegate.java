package com.parrot.freeflight.mediauploadservice.broadcastreceivers;

import com.parrot.freeflight.vo.MediaVO;

public interface MediaShareStateChangedReceiverDelegate {
    void onMediaShareStateChanged(MediaVO mediaVO, boolean z, double d);
}
