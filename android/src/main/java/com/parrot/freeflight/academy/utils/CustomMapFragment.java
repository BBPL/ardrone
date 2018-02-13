package com.parrot.freeflight.academy.utils;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.gms.maps.SupportMapFragment;

public class CustomMapFragment extends SupportMapFragment {
    private View originalView;
    private CustomMapLayout touchView;

    public View getView() {
        return this.originalView;
    }

    public boolean isMapTouched() {
        return this.touchView.isTouched();
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        this.originalView = super.onCreateView(layoutInflater, viewGroup, bundle);
        this.touchView = new CustomMapLayout(getActivity());
        this.touchView.addView(this.originalView);
        return this.touchView;
    }
}
