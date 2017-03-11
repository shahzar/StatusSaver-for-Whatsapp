package com.shzlabs.statussaver.ui.main.recentscreen;

import com.shzlabs.statussaver.data.local.FileHelper;
import com.shzlabs.statussaver.data.model.ImageModel;
import com.shzlabs.statussaver.ui.base.BasePresenter;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by shaz on 6/3/17.
 */

public class RecentPicsPresenter extends BasePresenter<RecentPicsView> {

    @Inject
    public FileHelper fileHelper;

    @Inject
    public RecentPicsPresenter(FileHelper fileHelper) {
        this.fileHelper = fileHelper;
    }

    void setLoadingAnimation(boolean status) {
        getMvpView().displayLoadingAnimation(status);
    }

    void loadRecentImages() {
        List<ImageModel> mediaItems = fileHelper.getRecentImages();
        if (!mediaItems.isEmpty()) {
            getMvpView().displayRecentImages(mediaItems);
        }else{
            getMvpView().displayNoImagesInfo();
        }
    }

    void saveMedia(ImageModel imageModel) {
        boolean status = fileHelper.saveMediaToLocalDir(imageModel);
        getMvpView().displayImageSavedMsg();
    }

    void loadImageViewer(ImageModel imageModel, int position) {
        getMvpView().displayImage(position, imageModel);
    }

}
