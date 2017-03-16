package com.shzlabs.statussaver.ui.base;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.shzlabs.statussaver.TheApplication;

public class BaseActivity extends AppCompatActivity {

    private FirebaseAnalytics mFirebaseAnalytics;

    public TheApplication getTheApplication() {
        return ((TheApplication) getApplication());
    }

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
    }
}