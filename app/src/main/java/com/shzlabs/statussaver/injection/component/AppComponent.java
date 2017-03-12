package com.shzlabs.statussaver.injection.component;


import com.shzlabs.statussaver.data.local.FileHelper;
import com.shzlabs.statussaver.injection.module.AppModule;
import com.shzlabs.statussaver.ui.imageslider.ImageSliderActivity;
import com.shzlabs.statussaver.ui.imageslider.imagedetails.ImageDetailsFragment;
import com.shzlabs.statussaver.ui.main.MainActivity;
import com.shzlabs.statussaver.ui.main.recentscreen.RecentPicsFragment;
import com.shzlabs.statussaver.ui.main.saved.SavedPicsFragment;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by shaz on 14/2/17.
 */

@Singleton
@Component(modules = AppModule.class)
public interface AppComponent {
    void inject(MainActivity activity);
    void inject(RecentPicsFragment fragment);
    void inject(SavedPicsFragment fragment);
    void inject(ImageSliderActivity activity);
    void inject(ImageDetailsFragment fragment);
    FileHelper fileHelper();
}
