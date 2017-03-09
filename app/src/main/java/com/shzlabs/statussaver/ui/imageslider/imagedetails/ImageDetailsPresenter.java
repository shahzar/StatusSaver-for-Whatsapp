package com.shzlabs.statussaver.ui.imageslider.imagedetails;

import com.shzlabs.statussaver.data.local.FileHelper;
import com.shzlabs.statussaver.ui.base.BasePresenter;

import javax.inject.Inject;

/**
 * Created by shaz on 14/2/17.
 */

public class ImageDetailsPresenter extends BasePresenter<ImageDetailsView> {

    private static final String TAG = ImageDetailsPresenter.class.getSimpleName();
    private final FileHelper fileHelper;

    @Inject
    public ImageDetailsPresenter(FileHelper fileHelper) {
        this.fileHelper = fileHelper;
    }

    void setLoadingAnimation(boolean status) {
        getMvpView().displayLoadingAnimation(status);
    }
}
