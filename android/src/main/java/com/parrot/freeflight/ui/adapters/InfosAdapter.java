package com.parrot.freeflight.ui.adapters;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.parrot.freeflight.utils.FontUtils;

public class InfosAdapter extends PagerAdapter {
    private int[] pages;

    public InfosAdapter(int[] iArr) {
        this.pages = iArr;
    }

    public void destroyItem(ViewGroup viewGroup, int i, Object obj) {
        ((ViewPager) viewGroup).removeView((View) obj);
    }

    public int getCount() {
        return this.pages.length;
    }

    public Object instantiateItem(ViewGroup viewGroup, int i) {
        ViewGroup viewGroup2 = (ViewGroup) ((LayoutInflater) viewGroup.getContext().getSystemService("layout_inflater")).inflate(this.pages[i], null);
        FontUtils.applyFont(viewGroup.getContext(), viewGroup2);
        ((ViewPager) viewGroup).addView(viewGroup2, 0);
        return viewGroup2;
    }

    public boolean isViewFromObject(View view, Object obj) {
        return view == obj;
    }
}
