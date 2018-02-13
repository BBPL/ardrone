package com.parrot.freeflight.activities;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.parrot.freeflight.C0984R;
import com.parrot.freeflight.utils.FontUtils;

public class MediaLoadingDialog extends DialogFragment implements OnClickListener {
    private static final String TAG = MediaLoadingDialog.class.getSimpleName();
    private MediaLoadingDialogDelegate delegate;
    private Runnable dismissCommand;
    private boolean isVideo;
    private Button mediaUploadCancelButton;
    private TextView mediaUploadStatus;
    private String mediaUploadStatusString = null;
    private ProgressBar pb;
    private ProgressBar pbIndeterminate;
    private Integer percentProgress = null;

    class C10931 implements Runnable {
        C10931() {
        }

        public void run() {
            if (MediaLoadingDialog.this.isVisible()) {
                MediaLoadingDialog.this.dismiss(false);
            }
        }
    }

    public void dismiss() {
        if (this.dismissCommand != null) {
            getView().getHandler().removeCallbacks(this.dismissCommand);
            this.dismissCommand = null;
        }
        super.dismiss();
    }

    public void dismiss(boolean z) {
        if (this.delegate != null) {
            if (z) {
                this.delegate.onDialogDidCancel();
            } else {
                this.delegate.onDialogClosed();
            }
        }
        dismiss();
    }

    public void dismissAfter(long j) {
        this.dismissCommand = new C10931();
        getView().postDelayed(this.dismissCommand, j);
    }

    public void onClick(View view) {
        if (view.getId() != C0984R.id.mediaUploadCancelButton) {
            return;
        }
        if (this.delegate != null) {
            this.delegate.onDialogWillCancel();
        } else {
            dismiss(true);
        }
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.dismissCommand = null;
        setStyle(1, 16973841);
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(C0984R.layout.media_uploading_progress, viewGroup, false);
        FontUtils.applyFont(getActivity(), inflate);
        this.pb = (ProgressBar) inflate.findViewById(C0984R.id.mediaUploadProgress);
        this.pbIndeterminate = (ProgressBar) inflate.findViewById(C0984R.id.mediaUploadProgressIndeterminate);
        this.mediaUploadStatus = (TextView) inflate.findViewById(C0984R.id.mediaUploadStatus);
        if (this.mediaUploadStatusString != null) {
            this.mediaUploadStatus.setText(this.mediaUploadStatusString);
        } else {
            this.mediaUploadStatus.setText(getString(C0984R.string.ff_ID000024));
        }
        ImageView imageView = (ImageView) inflate.findViewById(C0984R.id.mediaUploadImage);
        if (this.isVideo) {
            imageView.setImageResource(C0984R.drawable.ff2_0_loading_youtube);
        } else {
            imageView.setImageResource(C0984R.drawable.ff2_0_loading_picasa);
        }
        this.mediaUploadCancelButton = (Button) inflate.findViewById(C0984R.id.mediaUploadCancelButton);
        this.mediaUploadCancelButton.setOnClickListener(this);
        this.pb.setVisibility(0);
        this.pbIndeterminate.setVisibility(8);
        if (this.percentProgress != null) {
            setProgress(this.percentProgress.intValue());
            return inflate;
        }
        setProgress(0);
        return inflate;
    }

    public void onSaveInstanceState(Bundle bundle) {
        if (this.dismissCommand != null) {
            getView().getHandler().removeCallbacks(this.dismissCommand);
            this.dismissCommand = null;
        }
        super.onSaveInstanceState(bundle);
    }

    public void setDelegate(MediaLoadingDialogDelegate mediaLoadingDialogDelegate) {
        this.delegate = mediaLoadingDialogDelegate;
    }

    public void setIsVideo(boolean z) {
        this.isVideo = z;
    }

    public void setProgress(int i) {
        if (this.pb == null || this.pbIndeterminate == null) {
            this.percentProgress = Integer.valueOf(i);
        } else if (i < 0) {
            this.mediaUploadCancelButton.setVisibility(8);
            this.pb.setVisibility(8);
            this.pbIndeterminate.setVisibility(0);
        } else {
            Log.d(TAG, "Updating progress to " + i + "%");
            this.mediaUploadCancelButton.setVisibility(0);
            this.pb.setIndeterminate(false);
            this.pb.setVisibility(0);
            this.pbIndeterminate.setVisibility(8);
            this.pb.setProgress(i);
        }
    }

    public void setText(String str) {
        if (this.mediaUploadStatus == null) {
            this.mediaUploadStatusString = str;
        } else {
            this.mediaUploadStatus.setText(str);
        }
    }
}
