package com.shzlabs.statussaver.ui.imageslider;


import com.shzlabs.statussaver.data.model.ImageModel;
import com.shzlabs.statussaver.ui.base.MvpView;

import java.util.List;

/**
 * Created by shaz on 14/2/17.
 */

public interface ImageSliderView extends MvpView {
    void displayLoadingAnimation(boolean status);
    void displayImageSlider(List<ImageModel> mediaItems, int imageToDisplayPosition);
}
