package com.parrot.freeflight.mediauploadservice.broadcastreceivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import com.parrot.freeflight.mediauploadservice.MediaShareService;

public class MediaUploadStateChangedReceiver extends BroadcastReceiver {
    private MediaUploadStateChangedReceiverDelegate delegate;

    public MediaUploadStateChangedReceiver(MediaUploadStateChangedReceiverDelegate mediaUploadStateChangedReceiverDelegate) {
        this.delegate = mediaUploadStateChangedReceiverDelegate;
    }

    public static IntentFilter createFilter() {
        return new IntentFilter(MediaShareService.ACTION_UPLOAD_STATE_CHANGED);
    }

    public void onReceive(Context context, Intent intent) {
        if (this.delegate != null) {
            this.delegate.onMediaUploadStateChanged(intent.getBooleanExtra(MediaShareService.EXTRA_UPLOADING_STATE, false));
        }
    }
}
