package com.parrot.freeflight.activities;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.parrot.freeflight.C0984R;
import com.parrot.freeflight.utils.FontUtils;

public class WarningDialog extends DialogFragment implements OnClickListener {
    private static final String TAG = WarningDialog.class.getSimpleName();
    private int bottomViewSize;
    private Button button1;
    private OnClickListener button1Listener;
    private String button1Text;
    private Button button2;
    private OnClickListener button2Listener;
    private String button2Text;
    private Button button3;
    private OnClickListener button3Listener;
    private String button3Text;
    private WarningDialogDelegate delegate;
    private Runnable dismissCommand;
    private TextView message;
    private String messageStr;
    private ProgressBar pb;
    private boolean pbVisible;
    private int progress;
    private final boolean showCloseButton;
    private int time;
    private int topRightOffset;
    private int topViewSize;
    private int topWidth;
    private final boolean useButtonsLayout;

    class C11341 implements OnTouchListener {
        C11341() {
        }

        public boolean onTouch(View view, MotionEvent motionEvent) {
            return WarningDialog.this.getActivity().onTouchEvent(motionEvent);
        }
    }

    class C11352 implements OnTouchListener {
        C11352() {
        }

        public boolean onTouch(View view, MotionEvent motionEvent) {
            return true;
        }
    }

    class C11363 implements OnTouchListener {
        C11363() {
        }

        public boolean onTouch(View view, MotionEvent motionEvent) {
            return true;
        }
    }

    class C11374 implements OnTouchListener {
        C11374() {
        }

        public boolean onTouch(View view, MotionEvent motionEvent) {
            return true;
        }
    }

    class C11385 implements Runnable {
        C11385() {
        }

        public void run() {
            if (WarningDialog.this.isVisible()) {
                WarningDialog.this.dismiss(true);
            }
        }
    }

    public WarningDialog() {
        this.pbVisible = false;
        this.progress = 0;
        this.topViewSize = 0;
        this.bottomViewSize = 0;
        this.topWidth = 0;
        this.topRightOffset = 0;
        this.useButtonsLayout = false;
        this.showCloseButton = true;
    }

    public WarningDialog(boolean z, boolean z2) {
        this.pbVisible = false;
        this.progress = 0;
        this.topViewSize = 0;
        this.bottomViewSize = 0;
        this.topWidth = 0;
        this.topRightOffset = 0;
        this.useButtonsLayout = z;
        this.showCloseButton = z2;
    }

    private void initButton(Button button, String str, OnClickListener onClickListener) {
        if (button != null) {
            if (str != null) {
                button.setVisibility(0);
                button.setText(str);
            }
            if (onClickListener != null) {
                button.setOnClickListener(onClickListener);
            }
        }
    }

    public void dismiss(boolean z) {
        if (this.delegate != null) {
            if (z) {
                this.delegate.onDialogTimeout();
            } else {
                this.delegate.onDialogDismissed();
            }
        }
        super.dismiss();
    }

    public void onClick(View view) {
        if (view.getId() == C0984R.id.btnClose) {
            dismiss(false);
        }
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setStyle(1, 16973841);
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        int i = 0;
        View inflate = layoutInflater.inflate(this.useButtonsLayout ? C0984R.layout.hud_popup2 : C0984R.layout.hud_popup, viewGroup, false);
        FontUtils.applyFont(getActivity(), inflate);
        this.message = (TextView) inflate.findViewById(C0984R.id.txtMessage);
        this.message.setText(this.messageStr);
        this.pb = (ProgressBar) inflate.findViewById(C0984R.id.recordProgress);
        this.pb.setVisibility(this.pbVisible ? 0 : 4);
        this.pb.setProgress(this.progress);
        if (this.useButtonsLayout) {
            this.button1 = (Button) inflate.findViewById(C0984R.id.button1);
            initButton(this.button1, this.button1Text, this.button1Listener);
            this.button2 = (Button) inflate.findViewById(C0984R.id.button2);
            initButton(this.button2, this.button2Text, this.button2Listener);
            this.button3 = (Button) inflate.findViewById(C0984R.id.button3);
            initButton(this.button3, this.button3Text, this.button3Listener);
        }
        ImageButton imageButton = (ImageButton) inflate.findViewById(C0984R.id.btnClose);
        if (!this.showCloseButton) {
            i = 8;
        }
        imageButton.setVisibility(i);
        imageButton.setOnClickListener(this);
        inflate.setOnTouchListener(new C11341());
        View findViewById = inflate.findViewById(C0984R.id.hudPopUpTopLeft);
        findViewById.setOnTouchListener(new C11352());
        View findViewById2 = inflate.findViewById(C0984R.id.hudPopUpTopRight);
        findViewById2.setOnTouchListener(new C11363());
        View findViewById3 = inflate.findViewById(C0984R.id.hudPopUpBottom);
        findViewById3.setOnTouchListener(new C11374());
        MarginLayoutParams marginLayoutParams = (MarginLayoutParams) findViewById.getLayoutParams();
        marginLayoutParams.height = this.topViewSize;
        marginLayoutParams.width = this.topWidth;
        findViewById.setLayoutParams(marginLayoutParams);
        marginLayoutParams = (MarginLayoutParams) findViewById2.getLayoutParams();
        marginLayoutParams.height = this.topViewSize;
        marginLayoutParams.width = this.topWidth;
        marginLayoutParams.leftMargin = this.topRightOffset;
        findViewById2.setLayoutParams(marginLayoutParams);
        marginLayoutParams = (MarginLayoutParams) findViewById3.getLayoutParams();
        marginLayoutParams.height = this.bottomViewSize;
        findViewById3.setLayoutParams(marginLayoutParams);
        return inflate;
    }

    public void onSaveInstanceState(Bundle bundle) {
        getView().getHandler().removeCallbacks(this.dismissCommand);
        super.onSaveInstanceState(bundle);
    }

    public void onStart() {
        super.onStart();
        if (this.time != 0) {
            this.dismissCommand = new C11385();
            getView().postDelayed(this.dismissCommand, (long) this.time);
        }
        Log.d(TAG, "Warning Dialog on Start");
    }

    public void setBarSizes(int i, int i2, int i3, int i4) {
        this.topViewSize = i;
        this.bottomViewSize = i2;
        this.topWidth = (i3 - i4) / 2;
        this.topRightOffset = this.topWidth + i4;
    }

    public void setButton1(String str, OnClickListener onClickListener) {
        if (this.useButtonsLayout) {
            this.button1Text = str;
            this.button1Listener = onClickListener;
            initButton(this.button1, str, onClickListener);
            return;
        }
        throw new IllegalStateException("useButtonsLayout is set to false");
    }

    public void setButton2(String str, OnClickListener onClickListener) {
        if (this.useButtonsLayout) {
            this.button2Text = str;
            this.button2Listener = onClickListener;
            initButton(this.button2, str, onClickListener);
            return;
        }
        throw new IllegalStateException("useButtonsLayout is set to false");
    }

    public void setButton3(String str, OnClickListener onClickListener) {
        if (this.useButtonsLayout) {
            this.button3Text = str;
            this.button3Listener = onClickListener;
            initButton(this.button3, str, onClickListener);
            return;
        }
        throw new IllegalStateException("useButtonsLayout is set to false");
    }

    public void setDelegate(WarningDialogDelegate warningDialogDelegate) {
        this.delegate = warningDialogDelegate;
    }

    public void setDismissAfter(int i) {
        this.time = i;
    }

    public void setMessage(String str) {
        this.messageStr = str;
        if (this.message != null) {
            this.message.setText(str);
        }
    }

    public void setProgress(boolean z, int i) {
        if (this.pb == null) {
            this.pbVisible = z;
            this.progress = i;
            return;
        }
        if (z) {
            this.pb.setVisibility(0);
            this.pb.setProgress(i);
            View view = getView();
            if (view != null) {
                Handler handler = view.getHandler();
                if (handler != null) {
                    handler.removeCallbacks(this.dismissCommand);
                }
            }
        } else if (this.pbVisible) {
            dismiss(true);
        } else {
            this.pb.setVisibility(4);
        }
        this.pbVisible = z;
        this.progress = i;
    }
}
