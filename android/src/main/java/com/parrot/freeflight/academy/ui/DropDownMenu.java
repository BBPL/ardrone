package com.parrot.freeflight.academy.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import com.parrot.freeflight.C0984R;
import java.util.ArrayList;

public class DropDownMenu extends AnimatedView implements OnClickListener {
    private ArrayList<View> children;
    private Context context;
    private int index = 0;
    private LinearLayout linearLayout;
    private ScrollView scrollView;

    public DropDownMenu(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public DropDownMenu(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.context = context;
        init();
    }

    private View addSeparator() {
        View view = new View(this.context);
        view.setBackgroundResource(C0984R.drawable.ff2_2_section_separator);
        return view;
    }

    public void addView(View view, boolean z, Object obj) {
        LayoutParams layoutParams;
        view.setId(this.index);
        if (z) {
            layoutParams = new LinearLayout.LayoutParams(-1, 1);
        } else {
            if (this.index != 0) {
                addView(addSeparator(), true, null);
            }
            layoutParams = new LinearLayout.LayoutParams(-1, -2);
        }
        this.linearLayout.addView(view, this.index, layoutParams);
        view.setVisibility(0);
        if (!z) {
            view.setTag(obj);
            this.children.add(view);
        }
        this.index++;
    }

    public ArrayList<View> getChildren() {
        return this.children;
    }

    public void init() {
        setOrientation(1);
        setBackgroundColor(C0984R.color.black_half_transparent);
        this.children = new ArrayList();
        this.scrollView = new ScrollView(this.context);
        addView(this.scrollView, new LinearLayout.LayoutParams(-1, -2));
        this.linearLayout = new LinearLayout(this.context);
        this.linearLayout.setOrientation(1);
        this.scrollView.addView(this.linearLayout, new LinearLayout.LayoutParams(-1, -1));
        setOnClickListener(this);
    }

    public void onClick(View view) {
    }
}
