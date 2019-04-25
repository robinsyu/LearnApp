package com.example.yurs.learnapp;

import android.app.Application;
import android.content.Context;

/**
 * @ClassName: App
 * @Description: java类作用描述
 * @Author: yurs
 * @Date: 2019/4/24 20:35
 */
public class App extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    public static Context getContext() {
        return context;
    }
}
