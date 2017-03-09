package com.shzlabs.statussaver.ui.imageslider;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.shzlabs.statussaver.data.model.ImageModel;
import com.shzlabs.statussaver.ui.imageslider.imagedetails.ImageDetailsFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shaz on 8/3/17.
 */

public class CustomViewPagerAdapter extends FragmentStatePagerAdapter {

    private List<ImageModel> items;

    public CustomViewPagerAdapter(FragmentManager fm, List<ImageModel> items) {
        super(fm);
        this.items = items;
    }

    @Override
    public Fragment getItem(int position) {
        return ImageDetailsFragment.newInstance(items.get(position));
    }

    @Override
    public int getCount() {
        return items.size();
    }
}
