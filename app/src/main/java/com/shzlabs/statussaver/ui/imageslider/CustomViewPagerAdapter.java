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
    // Image types available in ImageSliderActivity
    private int imageType;

    public CustomViewPagerAdapter(FragmentManager fm, List<ImageModel> items, int imageType) {
        super(fm);
        this.items = items;
        this.imageType = imageType;
    }

    public void removeItem(int position) {
        items.remove(position);
        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {
        return ImageDetailsFragment.newInstance(items.get(position), imageType);
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}
