package com.parrot.freeflight.ui.adapters;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import java.util.List;

public class SettingsViewAdapter extends PagerAdapter {
    private List<View> views;

    public SettingsViewAdapter(List<View> list) {
        this.views = list;
    }

    public void destroyItem(ViewGroup viewGroup, int i, Object obj) {
        ((ViewPager) viewGroup).removeView((View) obj);
    }

    public int getCount() {
        return this.views.size();
    }

    public Object instantiateItem(ViewGroup viewGroup, int i) {
        View view = (View) this.views.get(i);
        ((ViewPager) viewGroup).addView(view, 0);
        return view;
    }

    public boolean isViewFromObject(View view, Object obj) {
        return view.equals(obj);
    }
}
