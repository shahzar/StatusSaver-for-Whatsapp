package com.shzlabs.statussaver;

import android.app.Application;

import com.shzlabs.statussaver.injection.component.AppComponent;
import com.shzlabs.statussaver.injection.component.DaggerAppComponent;
import com.shzlabs.statussaver.injection.module.AppModule;


public class TheApplication extends Application {

    AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }
}