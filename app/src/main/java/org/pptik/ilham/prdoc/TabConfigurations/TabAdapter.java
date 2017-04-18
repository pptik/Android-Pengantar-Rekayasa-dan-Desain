package org.pptik.ilham.prdoc.TabConfigurations;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;

import org.pptik.ilham.prdoc.FragmentCollections.BimbinganFragment;
import org.pptik.ilham.prdoc.FragmentCollections.MaterialsFragment;
import org.pptik.ilham.prdoc.FragmentCollections.ProfileFragment;
import org.pptik.ilham.prdoc.R;

/**
 * Created by Ilham on 4/11/17.
 * emilhamep@icloud.com
 * PPTIK Intitut Teknologi Bandung
 * Kelas ini digunakan sebagai adapter dari Sliding Tab Layout
 */

public class TabAdapter extends FragmentPagerAdapter {
    private Context context;
    public String[] labelsSlidingTab = {"Materi", "Bimbingan", "Profile"};
    public int[] icons = {R.mipmap.ic_bell_outline,R.mipmap.ic_bell_outline,R.mipmap.ic_bell_outline};
    private int tinggiIcon;

    public TabAdapter(FragmentManager fm, Context c) {
        super(fm);

        context = c;
        double scale = c.getResources().getDisplayMetrics().density;
        tinggiIcon = (int)(24 * scale + 0.5f);

    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;

        if(position == 0){
            fragment = new MaterialsFragment();
        } else if (position == 1) {
            fragment = new BimbinganFragment();
        } else if (position == 2) {
            fragment = new ProfileFragment();
        }

        //Bundle b = new Bundle();
        //b.putInt("posisi",position);

        //fragment.setArguments(b);

        return fragment;
    }

    //Count the number of displayed tab
    //Based on the length of icons's array
    @Override
    public int getCount() {
        return icons.length;
    }

    /*@Override
    public CharSequence getPageTitle(int position) {
        Drawable d = context.getResources().getDrawable(icons[position]);
        d.setBounds(0 ,0 , tinggiIcon, tinggiIcon);

        ImageSpan is = new ImageSpan(d);

        SpannableString sp = new SpannableString(" ");
        sp.setSpan(is,0,sp.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return (sp);
    }*/

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return context.getResources().getString(R.string.materials_label);
            case 1:
                return context.getResources().getString(R.string.guidance_label);
            case 2:
                return context.getResources().getString(R.string.profile_label);
        }
        return null;

    }
}
