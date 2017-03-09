package com.shzlabs.statussaver.ui.base;

import android.support.v7.app.AppCompatActivity;

import com.shzlabs.statussaver.TheApplication;

public class BaseActivity extends AppCompatActivity {

    public TheApplication getTheApplication() {
        return ((TheApplication) getApplication());
    }

}