package com.shzlabs.statussaver.ui.main.saved;

import com.shzlabs.statussaver.data.model.ImageModel;
import com.shzlabs.statussaver.ui.base.MvpView;

import java.util.List;

/**
 * Created by shaz on 6/3/17.
 */

public interface SavedPicsView extends MvpView{
    void displayLoadingAnimation(boolean status);
    void displaySavedImages(List<ImageModel> images);
    void displayNoImagesInfo();
    void displayImage(int position, ImageModel imageModel);
    void displayDeleteSuccessMsg();
    void displayDeleteConfirm(List<ImageModel> imageModels);
}
