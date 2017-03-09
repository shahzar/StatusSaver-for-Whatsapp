package com.shzlabs.statussaver.ui.main.recentscreen;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.shzlabs.statussaver.R;
import com.shzlabs.statussaver.data.model.ImageModel;
import com.shzlabs.statussaver.ui.base.BaseFragment;
import com.shzlabs.statussaver.ui.imageslider.ImageSliderActivity;
import com.shzlabs.statussaver.ui.main.ImageListAdapter;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.functions.Action1;

/**
 * Created by shaz on 6/3/17.
 */

public class RecentPicsFragment extends BaseFragment implements RecentPicsView {

    View rootView;

    @Inject
    RecentPicsPresenter presenter;
    @Inject
    ImageListAdapter adapter;
    @BindView(R.id.images_recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.msg_no_media_text_view)
    TextView noRecentImagesMsgTextView;

    public static RecentPicsFragment newInstance() {

        Bundle args = new Bundle();

        RecentPicsFragment fragment = new RecentPicsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        getTheApplication().getAppComponent().inject(this);
        rootView = inflater.inflate(R.layout.fragment_recent_pics, container, false);
        ButterKnife.bind(this, rootView);

        presenter.attachView(this);

        presenter.setLoadingAnimation(true);

        // Setup recycler view
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        adapter.getOnItemClicks().subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer position) {
//                presenter.saveMedia(adapter.getItemAtPosition(position));
                presenter.loadImageViewer(adapter.getItemAtPosition(position));
            }
        });
        recyclerView.setAdapter(adapter);

        presenter.setLoadingAnimation(true);

        presenter.loadRecentImages();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.loadRecentImages();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        return rootView;
    }

    @Override
    public void displayLoadingAnimation(boolean status) {
        if (status) {
            recyclerView.setVisibility(View.GONE);
            noRecentImagesMsgTextView.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
        }else{
            progressBar.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void displayRecentImages(List<ImageModel> images) {
        presenter.setLoadingAnimation(false);
        noRecentImagesMsgTextView.setVisibility(View.GONE);
        adapter.setItems(images);
    }

    @Override
    public void displayNoImagesInfo() {
        presenter.setLoadingAnimation(false);
        noRecentImagesMsgTextView.setVisibility(View.VISIBLE);
    }

    @Override
    public void displayImageSavedMsg() {
        Snackbar.make(rootView, "Image saved", Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void displayImage(ImageModel imageModel) {
        Intent intent = new Intent(getActivity(), ImageSliderActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(ImageSliderActivity.INTENT_IMAGE_TYPE, ImageSliderActivity.IMAGES_TYPE_RECENT);
        bundle.putParcelable(ImageSliderActivity.INTENT_IMAGE_DATA, imageModel);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
