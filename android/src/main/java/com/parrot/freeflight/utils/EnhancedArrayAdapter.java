package com.parrot.freeflight.utils;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public class EnhancedArrayAdapter<T> extends ArrayAdapter<T> {
    public EnhancedArrayAdapter(Context context, int i) {
        super(context, i);
    }

    public EnhancedArrayAdapter(Context context, int i, T[] tArr) {
        super(context, i, tArr);
    }

    public View getDropDownView(int i, View view, ViewGroup viewGroup) {
        View dropDownView = super.getDropDownView(i, view, viewGroup);
        FontUtils.applyFont(getContext(), dropDownView);
        return dropDownView;
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        View view2 = super.getView(i, view, viewGroup);
        FontUtils.applyFont(getContext(), view2);
        return view2;
    }
}
