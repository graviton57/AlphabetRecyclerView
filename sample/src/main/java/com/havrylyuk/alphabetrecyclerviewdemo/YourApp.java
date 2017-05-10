package com.havrylyuk.alphabetrecyclerviewdemo;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;

/**
 * Created by Igor Havrylyuk on 08.03.2017.
 */

public class YourApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);
    }
}
