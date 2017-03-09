package com.shzlabs.statussaver.ui.main;


import com.shzlabs.statussaver.ui.base.MvpView;

/**
 * Created by shaz on 14/2/17.
 */

public interface MainView extends MvpView {
    void displayWelcomeMessage(String msg);
    void displayLoadingAnimation(boolean status);
    void displayRecentAndSavedPics();
}
