package com.example.gradle_config;

import androidx.multidex.MultiDexApplication;

public class BaseApplication extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();

//        MultiDex.install(this);
    }

}