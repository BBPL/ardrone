package com.parrot.freeflight.mediauploadservice.broadcastreceivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import com.parrot.freeflight.mediauploadservice.MediaShareService;
import com.parrot.freeflight.vo.MediaVO;

public class MediaShareStateChangedReceiver extends BroadcastReceiver {
    private MediaShareStateChangedReceiverDelegate delegate;

    public MediaShareStateChangedReceiver(MediaShareStateChangedReceiverDelegate mediaShareStateChangedReceiverDelegate) {
        this.delegate = mediaShareStateChangedReceiverDelegate;
    }

    public static IntentFilter createFilter() {
        return new IntentFilter(MediaShareService.ACTION_UPLOAD_STATE_CHANGED);
    }

    public void onReceive(Context context, Intent intent) {
        if (this.delegate != null) {
            MediaVO mediaVO = (MediaVO) intent.getParcelableExtra(MediaShareService.EXTRA_UPLOADING_MEDIA);
            this.delegate.onMediaShareStateChanged(mediaVO, intent.getBooleanExtra(MediaShareService.EXTRA_UPLOADING_STATE, false), intent.getDoubleExtra(MediaShareService.EXTRA_UPLOADING_PROGRESS, 0.0d));
        }
    }
}
