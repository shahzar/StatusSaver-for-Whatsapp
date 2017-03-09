package com.shzlabs.statussaver.ui.main;

import com.shzlabs.statussaver.data.local.FileHelper;
import com.shzlabs.statussaver.ui.base.BasePresenter;

import javax.inject.Inject;

/**
 * Created by shaz on 14/2/17.
 */

public class MainPresenter extends BasePresenter<MainView> {

    private static final String TAG = MainPresenter.class.getSimpleName();

    @Inject
    public MainPresenter() {
    }

    void loadWelcomeMessage() {
        getMvpView().displayWelcomeMessage("Hello world!");
    }

    void setLoadingAnimation(boolean status) {
        getMvpView().displayLoadingAnimation(status);
    }

    void loadRecentAndSavedPics() {
        getMvpView().displayRecentAndSavedPics();
    }

}
