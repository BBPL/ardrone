package com.parrot.freeflight.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import com.parrot.freeflight.service.DroneControlService;
import com.parrot.freeflight.transcodeservice.TranscodingService;
import java.io.File;

public class MediaReadyReceiver extends BroadcastReceiver {
    private MediaReadyDelegate delegate;

    public MediaReadyReceiver(MediaReadyDelegate mediaReadyDelegate) {
        this.delegate = mediaReadyDelegate;
    }

    public static IntentFilter createFilter() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(DroneControlService.ACTION_NEW_MEDIA_IS_AVAILABLE);
        intentFilter.addAction(TranscodingService.NEW_MEDIA_IS_AVAILABLE_ACTION);
        return intentFilter;
    }

    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals(DroneControlService.ACTION_NEW_MEDIA_IS_AVAILABLE) || action.equals(TranscodingService.NEW_MEDIA_IS_AVAILABLE_ACTION)) {
            action = intent.getStringExtra(DroneControlService.EXTRA_MEDIA_PATH);
            if (action == null) {
                action = intent.getStringExtra(TranscodingService.EXTRA_MEDIA_PATH);
            }
            if (this.delegate != null && action != null) {
                this.delegate.onMediaReady(new File(action));
            }
        }
    }
}
