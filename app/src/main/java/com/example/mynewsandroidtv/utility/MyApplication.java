package com.example.mynewsandroidtv.utility;

import android.app.Application;

import com.example.mynewsandroidtv.model.MainDataModel;


public class MyApplication extends Application {

    public static MyApplication singleton;
    public static MainDataModel mainDataModel;

    public static MainDataModel getMainDataModel() {
        return mainDataModel;
    }

    public static void setMainDataModel(MainDataModel mainDataModel) {
        MyApplication.mainDataModel = mainDataModel;
    }

    public static MyApplication getInstance() {
        if (null == singleton) {
            singleton = new MyApplication();
        }
        return singleton;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        singleton = this;
    }
}
