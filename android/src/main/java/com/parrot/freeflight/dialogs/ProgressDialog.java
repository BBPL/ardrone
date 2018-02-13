package com.parrot.freeflight.dialogs;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.parrot.freeflight.C0984R;
import com.parrot.freeflight.utils.FontUtils;

public class ProgressDialog extends android.app.ProgressDialog implements OnClickListener {
    private static final int MESSAGE_SHOW_DIALOG = 1;
    private Button btnCancel;
    private final Handler handler;
    private boolean isCancelable;
    private ProgressBar progress;
    private CharSequence strSubTitle;
    private CharSequence strTitle;
    private TextView subTitle;
    private TextView title;

    class C11581 extends Handler {
        C11581() {
        }

        public void dispatchMessage(Message message) {
            super.dispatchMessage(message);
            ProgressDialog.this.onMessageReceived(message);
        }
    }

    public ProgressDialog(Context context) {
        this(context, C0984R.style.FreeFlightDialog_Light);
    }

    public ProgressDialog(Context context, int i) {
        super(context, i);
        this.isCancelable = true;
        this.handler = new C11581();
    }

    private void onMessageReceived(Message message) {
        switch (message.what) {
            case 1:
                if (!isShowing()) {
                    show();
                    return;
                }
                return;
            default:
                return;
        }
    }

    public void cancel() {
        this.handler.removeMessages(1);
        super.cancel();
    }

    public void dismiss() {
        this.handler.removeMessages(1);
        super.dismiss();
    }

    public void incrementProgressBy(int i) {
        if (this.progress != null) {
            this.progress.incrementProgressBy(i);
        }
    }

    public void incrementSecondaryProgressBy(int i) {
        if (this.progress != null) {
            this.progress.incrementSecondaryProgressBy(i);
        }
    }

    public boolean isIndeterminate() {
        return this.progress != null ? this.progress.isIndeterminate() : false;
    }

    public void onClick(View view) {
        if (view.getId() == C0984R.id.dialog_progress_button_cancel) {
            cancel();
        }
    }

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(C0984R.layout.dialog_progress);
        this.progress = (ProgressBar) findViewById(C0984R.id.dialog_progress_progressbar);
        this.title = (TextView) findViewById(C0984R.id.dialog_progress_textview_title);
        this.subTitle = (TextView) findViewById(C0984R.id.dialog_progress_textview_subtitle);
        this.btnCancel = (Button) findViewById(C0984R.id.dialog_progress_button_cancel);
        setCancelable(this.isCancelable);
        this.btnCancel.setVisibility(this.isCancelable ? 0 : 4);
        this.btnCancel.setOnClickListener(this);
        if (this.strTitle != null) {
            this.title.setText(this.strTitle);
        }
        if (this.strSubTitle != null) {
            this.subTitle.setText(this.strSubTitle);
        }
        onPostCreate();
    }

    protected void onPostCreate() {
        FontUtils.applyFont(getContext(), (ViewGroup) findViewById(16908290));
    }

    public void onStart() {
        super.onStart();
    }

    protected void onStop() {
        super.onStop();
    }

    public void setCancelable(boolean z) {
        super.setCancelable(z);
        this.isCancelable = z;
        if (this.btnCancel != null) {
            this.btnCancel.setVisibility(z ? 0 : 4);
        }
    }

    public void setIndeterminate(boolean z) {
        super.setIndeterminate(z);
        if (this.progress != null) {
            this.progress.setIndeterminate(z);
        }
    }

    public void setMax(int i) {
        super.setMax(i);
        this.progress.setMax(i);
    }

    public void setMessage(CharSequence charSequence) {
        super.setMessage(charSequence);
        if (this.title != null) {
            this.title.setText(charSequence);
        }
        this.strTitle = charSequence;
    }

    public void setProgress(int i) {
        if (this.progress != null) {
            this.progress.setProgress(i);
        }
    }

    public void setSubMessage(CharSequence charSequence) {
        if (this.subTitle != null) {
            this.subTitle.setText(charSequence);
        }
        this.strSubTitle = charSequence;
    }

    public void showDelayed(long j) {
        this.handler.sendEmptyMessageDelayed(1, j);
    }
}
