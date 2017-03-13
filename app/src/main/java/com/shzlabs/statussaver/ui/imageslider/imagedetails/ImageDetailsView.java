package com.shzlabs.statussaver.ui.imageslider.imagedetails;


import com.shzlabs.statussaver.data.model.ImageModel;
import com.shzlabs.statussaver.ui.base.MvpView;

/**
 * Created by shaz on 14/2/17.
 */

public interface ImageDetailsView extends MvpView {
    void displayLoadingAnimation(boolean status);
    void displayImageSavedMsg();
    void displayDeleteSuccessMsg();
}
