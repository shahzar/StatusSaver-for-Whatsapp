package com.shzlabs.statussaver.ui.imageslider.imagedetails;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.shzlabs.statussaver.R;
import com.shzlabs.statussaver.data.model.ImageModel;
import com.shzlabs.statussaver.ui.base.BaseFragment;

import java.io.File;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ImageDetailsFragment extends BaseFragment implements ImageDetailsView {

    @Inject
    ImageDetailsPresenter presenter;
    View rootView;
    @BindView(R.id.image_view)
    ImageView imageView;

    public static ImageDetailsFragment newInstance(ImageModel imageModel) {

        Bundle args = new Bundle();
        args.putParcelable("imageData", imageModel);
        ImageDetailsFragment fragment = new ImageDetailsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_image_details, container, false);
        getTheApplication().getAppComponent().inject(this);
        ButterKnife.bind(this, rootView);

        presenter.attachView(this);

        ImageModel imageModel = getArguments().getParcelable("imageData");

        Glide.with(getActivity())
                .load(new File(imageModel.getCompletePath()))
                .into(imageView);

        return rootView;
    }


    @Override
    public void displayLoadingAnimation(boolean status) {

    }


}
