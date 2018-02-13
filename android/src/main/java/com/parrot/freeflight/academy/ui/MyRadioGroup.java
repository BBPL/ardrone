package com.parrot.freeflight.academy.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewDebug.ExportedProperty;
import android.view.ViewGroup.LayoutParams;
import android.widget.CheckedTextView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import com.parrot.freeflight.C0984R;
import java.util.ArrayList;

public class MyRadioGroup extends RadioGroup implements OnCheckedChangeListener, OnClickListener {
    private MyCheckedTextView checkedButton = null;
    private ArrayList<MyCheckedTextView> children = new ArrayList();
    private Context context;
    private int index = 0;

    public MyRadioGroup(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public MyRadioGroup(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.context = context;
        init();
    }

    private View addSeparator() {
        View view = new View(this.context);
        view.setBackgroundResource(C0984R.drawable.ff2_2_cell_separator);
        return view;
    }

    public void addView(View view, boolean z, Object obj) {
        LayoutParams layoutParams = z ? new RadioGroup.LayoutParams(-1, 1) : new RadioGroup.LayoutParams(-1, -2);
        view.setId(this.index);
        if (!(z || this.index == 0)) {
            addView(addSeparator(), true, null);
        }
        super.addView(view, this.index, layoutParams);
        if (!z) {
            view.setTag(obj);
            this.children.add((MyCheckedTextView) view);
        }
        this.index++;
    }

    public MyCheckedTextView getCheckedButton() {
        return this.checkedButton;
    }

    public ArrayList<MyCheckedTextView> getChildren() {
        return this.children;
    }

    @ExportedProperty
    public Object getTag() {
        return this.checkedButton.getTag();
    }

    public void init() {
        setOnCheckedChangeListener(this);
    }

    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        for (int i2 = 0; i2 < radioGroup.getChildCount(); i2++) {
            if (radioGroup.getChildAt(i2) instanceof CheckedTextView) {
                CheckedTextView checkedTextView = (CheckedTextView) radioGroup.getChildAt(i2);
                if (checkedTextView.isChecked() && checkedTextView.getId() != i) {
                    checkedTextView.toggle();
                }
                checkedTextView.setChecked(checkedTextView.getId() == i);
                if (checkedTextView.isChecked()) {
                    this.checkedButton = (MyCheckedTextView) checkedTextView;
                    checkedTextView.setEnabled(false);
                } else {
                    checkedTextView.setEnabled(true);
                }
            }
        }
    }

    public void onClick(View view) {
    }
}
