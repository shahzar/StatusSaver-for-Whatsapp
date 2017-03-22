package com.shzlabs.statussaver.ui.main.recentscreen;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.shzlabs.statussaver.R;
import com.shzlabs.statussaver.data.model.ImageModel;
import com.shzlabs.statussaver.ui.base.BaseFragment;
import com.shzlabs.statussaver.ui.imageslider.ImageSliderActivity;
import com.shzlabs.statussaver.util.DialogFactory;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.functions.Action1;

import static com.shzlabs.statussaver.ui.imageslider.ImageSliderActivity.EXTRA_IMAGE_TRANSITION_NAME;

/**
 * Created by shaz on 6/3/17.
 */

public class RecentPicsFragment extends BaseFragment implements RecentPicsView {

    private static final String TAG = RecentPicsFragment.class.getSimpleName();
    View rootView;
    @Inject
    RecentPicsPresenter presenter;
    @Inject
    RecentImageListAdapter adapter;
    @BindView(R.id.images_recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.msg_no_media_text_view)
    TextView noRecentImagesMsgTextView;
    private GridLayoutManager layoutManager;

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
        layoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(layoutManager);

        adapter.getOnItemClicks().subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer position) {
//                presenter.saveMedia(adapter.getItemAtPosition(position));
                presenter.loadImageViewer(adapter.getItemAtPosition(position), position);
            }
        });
        adapter.getOnSaveItemClicks().subscribe(new Action1<Integer>() {
            @Override
            public void call(final Integer position) {
                // Get the save icon of current item
                final View view = layoutManager.findViewByPosition(position);
                adapter.showSaveProgress(view);

                 new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        presenter.saveMedia(adapter.getItemAtPosition(position));
                        adapter.showDeleteButton(view);
                    }
                }, 500);

            }
        });
        adapter.getOnDeleteItemClicks().subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer position) {
                presenter.confirmDeleteAction(adapter.getItemAtPosition(position), position);
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
        recyclerView.setVisibility(View.VISIBLE);
        presenter.setLoadingAnimation(false);
        noRecentImagesMsgTextView.setVisibility(View.GONE);
        adapter.setItems(images);
    }

    @Override
    public void displayNoImagesInfo() {
        presenter.setLoadingAnimation(false);
        noRecentImagesMsgTextView.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
    }

    @Override
    public void displayImageSavedMsg() {
        Snackbar.make(rootView, "Image saved", Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void displayImage(int position, ImageModel imageModel) {

        // Get ImageView at current position, to apply transition effect
        View view = layoutManager.findViewByPosition(position);
        ImageView imageView = (ImageView) view.findViewById(R.id.image_thumbnail);

        Intent intent = new Intent(getActivity(), ImageSliderActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(ImageSliderActivity.INTENT_IMAGE_TYPE, ImageSliderActivity.IMAGES_TYPE_RECENT);
        bundle.putParcelable(ImageSliderActivity.INTENT_IMAGE_DATA, imageModel);
        intent.putExtras(bundle);
        intent.putExtra(EXTRA_IMAGE_TRANSITION_NAME, ViewCompat.getTransitionName(imageView));

        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                getActivity(),
                imageView,
                ViewCompat.getTransitionName(imageView));

        startActivity(intent, options.toBundle());
    }

    @Override
    public void displayDeleteSuccessMsg() {
        Snackbar.make(rootView, "Image removed from saved items", Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void displayDeleteConfirmPrompt(final ImageModel imageModel, final int itemPosition) {

        String title = getString(R.string.title_delete_confirm_dialog);
        String msg = getString(R.string.msg_alert_delete_item_confirm);

        DialogFactory.createOKCancelDialog(getActivity(), title, msg, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Ok
                        adapter.showSaveButton(layoutManager.findViewByPosition(itemPosition));
                        presenter.deleteLocalImage(imageModel);
                    }
                },
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Cancel
                        dialog.dismiss();
                    }
                }).show();
    }
}
