package com.shzlabs.statussaver.ui.main.saved;

import com.shzlabs.statussaver.data.local.FileHelper;
import com.shzlabs.statussaver.data.model.ImageModel;
import com.shzlabs.statussaver.ui.base.BasePresenter;

import java.util.List;

import javax.inject.Inject;

import rx.functions.Action1;

/**
 * Created by shaz on 6/3/17.
 */

public class SavedPicsPresenter extends BasePresenter<SavedPicsView> {

    @Inject
    public FileHelper fileHelper;

    @Inject
    public SavedPicsPresenter(FileHelper fileHelper) {
        this.fileHelper = fileHelper;
    }

    void setLoadingAnimation(boolean status) {
        getMvpView().displayLoadingAnimation(status);
    }

    void loadSavedImages() {
        List<ImageModel> mediaItems = fileHelper.getSavedImages();
        if (!mediaItems.isEmpty()) {
            getMvpView().displaySavedImages(mediaItems);
        }else{
            getMvpView().displayNoImagesInfo();
        }

        fileHelper.getMediaStateObservable().subscribe(new Action1<ImageModel>() {
            @Override
            public void call(ImageModel imageModel) {
                List<ImageModel> mediaItems = fileHelper.getSavedImages();
                if (!mediaItems.isEmpty()) {
                    getMvpView().displaySavedImages(mediaItems);
                }else{
                    getMvpView().displayNoImagesInfo();
                }
            }
        });
    }

    void loadImageViewer(ImageModel imageModel, int position) {
        getMvpView().displayImage(position, imageModel);
    }

    void deleteLocalImages(List<ImageModel> imageModels) {
        for (int i = 0; i < imageModels.size(); i++) {
            fileHelper.deleteImageFromLocalDir(imageModels.get(i));
        }
        getMvpView().displayDeleteSuccessMsg();
    }

    public void confirmDeleteAction(List<ImageModel> imageModels) {
        getMvpView().displayDeleteConfirm(imageModels);
    }
}
