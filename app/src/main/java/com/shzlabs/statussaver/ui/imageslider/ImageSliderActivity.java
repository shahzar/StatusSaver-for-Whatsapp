package com.shzlabs.statussaver.ui.imageslider;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.shzlabs.statussaver.R;
import com.shzlabs.statussaver.data.model.ImageModel;
import com.shzlabs.statussaver.ui.base.BaseActivity;
import com.shzlabs.statussaver.ui.imageslider.imagedetails.ImageDetailsFragment;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.functions.Action1;
import rx.subjects.PublishSubject;

public class ImageSliderActivity extends BaseActivity implements ImageSliderView {

    private static final String TAG = ImageSliderActivity.class.getSimpleName();
    public static final String INTENT_IMAGE_DATA = "imageData";
    public static final String INTENT_IMAGE_TYPE = "imageType";
    public static final String EXTRA_IMAGE_TRANSITION_NAME = "imageTransitionName";
    public static final int IMAGES_TYPE_RECENT = 0;
    public static final int IMAGES_TYPE_SAVED = 1;

    @Inject
    ImageSliderPresenter presenter;
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private CustomViewPagerAdapter adapter;
    private int imageType = -1;
    private ImageModel imageModel;
    private PublishSubject<ImageModel> imageDeletedSubject = PublishSubject.create();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getTheApplication().getAppComponent().inject(this);
        setContentView(R.layout.activity_image_details);
        ButterKnife.bind(this);
        supportPostponeEnterTransition();

        setSupportActionBar(toolbar);
        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (Exception e) {
            e.printStackTrace();
        }

        imageModel = null;
        if (getIntent().getExtras() != null) {
            imageModel = getIntent().getExtras().getParcelable(INTENT_IMAGE_DATA);
            imageType = getIntent().getExtras().getInt(INTENT_IMAGE_TYPE);
        }else{
            Log.e(TAG, "onCreate: Please pass image data in bundle");
            finish();
        }

        presenter.attachView(this);
        if (imageType == IMAGES_TYPE_RECENT) {
            presenter.loadRecentImageSlider(imageModel);
        }else{
            presenter.loadSavedImageSlider(imageModel);
        }

        imageDeletedSubject.subscribe(new Action1<ImageModel>() {
            @Override
            public void call(ImageModel imageModel) {
                if (imageType == IMAGES_TYPE_SAVED) {
                    int currentItemIndex = viewPager.getCurrentItem();
                    adapter.removeItem(currentItemIndex);
                }
            }
        });

    }

    @Override
    public void displayLoadingAnimation(boolean status) {

    }

    @Override
    public void displayImageSlider(List<ImageModel> mediaItems, int position) {
        // Setup viewpager
        adapter = new CustomViewPagerAdapter(getSupportFragmentManager(), mediaItems, imageType);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(position);
    }

    public PublishSubject<ImageModel> getImageDeletedSubject() {
        return imageDeletedSubject;
    }

}
