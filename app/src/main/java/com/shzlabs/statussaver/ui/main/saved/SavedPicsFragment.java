package com.shzlabs.statussaver.ui.main.saved;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.functions.Action1;

import static com.shzlabs.statussaver.ui.imageslider.ImageSliderActivity.EXTRA_IMAGE_TRANSITION_NAME;

/**
 * Created by shaz on 6/3/17.
 */

public class SavedPicsFragment extends BaseFragment implements SavedPicsView {

    private static final String TAG = SavedPicsFragment.class.getSimpleName();
    View rootView;
    @Inject
    SavedPicsPresenter presenter;
    @Inject
    SavedImageListAdapter adapter;
    @BindView(R.id.images_recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.msg_no_media_text_view)
    TextView noMediaMsgTextView;
    private GridLayoutManager layoutManager;

    public static SavedPicsFragment newInstance() {

        Bundle args = new Bundle();

        SavedPicsFragment fragment = new SavedPicsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        getTheApplication().getAppComponent().inject(this);
        rootView = inflater.inflate(R.layout.fragment_saved_pics, container, false);
        ButterKnife.bind(this, rootView);

        presenter.attachView(this);

        presenter.setLoadingAnimation(true);

        // Setup recycler view
        layoutManager = new GridLayoutManager(getActivity(), 3);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        adapter.getOnItemClicks().subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer position) {
            if (adapter.isSelectItemsOn()) {
                adapter.toggleSelected(position);
                if (adapter.getSelectedItemsList().isEmpty()) {
                    adapter.setSelectItemsOn(false);
                }
            }else{
                presenter.loadImageViewer(adapter.getItemAtPosition(position), position);
            }
            }
        });
        adapter.getOnLongItemClicks().subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer position) {
                if (!adapter.isSelectItemsOn()) {
                    adapter.setSelectItemsOn(true);
                    adapter.toggleSelected(position);
                }
            }
        });

        adapter.getOnSelectItemsStatusChange().subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean selectItemsIsOn) {
                try {
                    if (selectItemsIsOn) {
                        setHasOptionsMenu(true);
                        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);

                    }else{
                        setHasOptionsMenu(false);
                        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(true);
                    }
                } catch (Exception e) {
                    Log.e(TAG, "call: Error messing with actionbar from a fragment");
                    e.printStackTrace();
                }
            }
        });

        presenter.setLoadingAnimation(true);

        presenter.loadSavedImages();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.loadSavedImages();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        return rootView;
    }

    @Override
    public void displayLoadingAnimation(boolean status) {
        if (status) {
            recyclerView.setVisibility(View.GONE);
            noMediaMsgTextView.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
        }else{
            progressBar.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void displaySavedImages(List<ImageModel> images) {
        recyclerView.setVisibility(View.VISIBLE);
        presenter.setLoadingAnimation(false);
        noMediaMsgTextView.setVisibility(View.GONE);
        adapter.setItems(images);
    }

    @Override
    public void displayNoImagesInfo() {
        presenter.setLoadingAnimation(false);
        recyclerView.setVisibility(View.GONE);
        noMediaMsgTextView.setVisibility(View.VISIBLE);
    }

    @Override
    public void displayImage(int position, ImageModel imageModel) {

        // Get ImageView at current position, to apply transition effect
        View view = layoutManager.findViewByPosition(position);
        ImageView imageView = (ImageView) view.findViewById(R.id.image_thumbnail);

        Intent intent = new Intent(getActivity(), ImageSliderActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(ImageSliderActivity.INTENT_IMAGE_TYPE, ImageSliderActivity.IMAGES_TYPE_SAVED);
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
        Snackbar.make(rootView, "Deleted", Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void displayDeleteConfirm(final List<ImageModel> imageModels) {

        String title = getString(R.string.title_delete_confirm_dialog);
        String msg = "";

        if (imageModels.size() == 1) {
            msg = getString(R.string.msg_alert_delete_item_confirm);
        }else{
            msg = imageModels.size() + " items will be removed. Are you sure?";
        }
        DialogFactory.createOKCancelDialog(getActivity(), title, msg, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Ok
                        presenter.deleteLocalImages(imageModels);
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.item_selected_contextual_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.delete: {
                // Delete selected items
                List<Integer> list = adapter.getSelectedItemsList();
                if (!list.isEmpty()) {
                    List<ImageModel> imageModels = new ArrayList<>();
                    for (int i = 0; i < list.size(); i++) {
                        ImageModel imageModel = adapter.getItemAtPosition(list.get(i));
                        imageModels.add(imageModel);
                    }
                    presenter.confirmDeleteAction(imageModels);
                }
                adapter.setSelectItemsOn(false);
                presenter.loadSavedImages();
                break;
            }
            case android.R.id.home: {
                adapter.setSelectItemsOn(false);
            }
        }
        return true;
    }
}
