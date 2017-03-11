package com.shzlabs.statussaver.ui.imageslider.imagedetails;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        postponeEnterTransition();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setSharedElementEnterTransition(TransitionInflater.from(getContext()).inflateTransition(android.R.transition.move));
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_image_details, container, false);
        getTheApplication().getAppComponent().inject(this);
        ButterKnife.bind(this, rootView);

        presenter.attachView(this);

        ImageModel imageModel = getArguments().getParcelable("imageData");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            imageView.setTransitionName(imageModel.getFileName());
        }

        Glide.with(getActivity())
                .load(new File(imageModel.getCompletePath()))
                .listener(new RequestListener<File, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, File model, Target<GlideDrawable> target, boolean isFirstResource) {
                        startPostponedEnterTransition();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, File model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        startPostponedEnterTransition();
                        getActivity().supportStartPostponedEnterTransition();
                        return false;
                    }
                })
                .into(imageView);

        return rootView;
    }


    @Override
    public void displayLoadingAnimation(boolean status) {

    }


}
