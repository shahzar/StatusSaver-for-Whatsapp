package com.shzlabs.statussaver.ui.base;

import android.support.v4.app.Fragment;

import com.shzlabs.statussaver.TheApplication;

public class BaseFragment extends Fragment {

    public TheApplication getTheApplication() {
        return ((TheApplication) getActivity().getApplication());
    }

}