package com.example.gradle_config;

import android.support.multidex.MultiDexApplication;

public class BaseApplication extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();

//        MultiDex.install(this);
    }

}