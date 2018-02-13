package com.parrot.freeflight.academy.ui;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckedTextView;
import com.parrot.freeflight.C0984R;
import com.parrot.freeflight.utils.FontUtils.TYPEFACE;

public class MyCheckedTextView extends CheckedTextView implements OnClickListener {
    private boolean broadcasting;
    private int checkedResourceId;
    private OnClickListener clickListener;
    private Context context;
    private int drawableLeftId;
    GradientDrawable gradient;
    private boolean isRadio;
    private OnCheckedChangeListener listener;
    private int textId;

    public interface OnCheckedChangeListener {
        void onCheckedChanged(MyCheckedTextView myCheckedTextView, boolean z);
    }

    public MyCheckedTextView(Context context) {
        super(context);
        this.context = context;
    }

    public MyCheckedTextView(Context context, int i, int i2, int i3, boolean z, OnClickListener onClickListener) {
        super(context);
        this.context = context;
        this.drawableLeftId = i;
        this.checkedResourceId = i2;
        this.textId = i3;
        this.isRadio = z;
        this.clickListener = onClickListener;
        setText(this.textId);
        setTextSize(15.0f);
        setClickable(true);
        setPadding(15, 15, 15, 15);
        setCheckMarkDrawable(this.checkedResourceId);
        setGravity(16);
        initWithStyle();
    }

    public MyCheckedTextView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public MyCheckedTextView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public void addDrawable() {
        setCompoundDrawablesWithIntrinsicBounds(new BitmapDrawable(getResources(), BitmapFactory.decodeResource(getResources(), this.drawableLeftId)), null, null, null);
    }

    public void initWithStyle() {
        setTextColor(-1);
        setTypeface(TYPEFACE.Helvetica(this.context), 1);
        setBackgroundColor(-16777216);
        this.gradient = new GradientDrawable(Orientation.LEFT_RIGHT, new int[]{-16777216, C0984R.color.white_half_transparent, 0});
        setOnClickListener(this.clickListener);
    }

    public boolean isRadio() {
        return this.isRadio;
    }

    public void onClick(View view) {
        if (view.getParent() instanceof MyRadioGroup) {
            ((MyRadioGroup) view.getParent()).check(view.getId());
            ((MyRadioGroup) view.getParent()).onClick(view);
        }
        if (view.getParent() instanceof DropDownMenu) {
            ((DropDownMenu) view.getParent()).onClick(view);
        }
    }

    public boolean performClick() {
        toggle();
        return super.performClick();
    }

    public void setChecked(boolean z) {
        super.setChecked(z);
        if (!this.broadcasting) {
            this.broadcasting = true;
            if (this.listener != null) {
                this.listener.onCheckedChanged(this, isChecked());
            }
            this.broadcasting = false;
            if (z) {
                setBackgroundDrawable(this.gradient);
            } else {
                setBackgroundColor(-16777216);
            }
        }
    }

    public void setOnCheckedChangeListener(OnCheckedChangeListener onCheckedChangeListener) {
        this.listener = onCheckedChangeListener;
    }

    public void toggle() {
        super.toggle();
        if (isChecked()) {
            setBackgroundDrawable(this.gradient);
        } else {
            setBackgroundColor(-16777216);
        }
    }
}
