package com.timotiusoktorio.pencake;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;

import com.timotiusoktorio.pencake.di.AppComponent;
import com.timotiusoktorio.pencake.di.AppModule;
import com.timotiusoktorio.pencake.di.DaggerAppComponent;

import timber.log.Timber;

public class MyApplication extends Application {

    private AppComponent component;

    @Override
    public void onCreate() {
        super.onCreate();
        component = createComponent();
        Timber.plant(new Timber.DebugTree());
    }

    private AppComponent createComponent() {
        return DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
    }

    public AppComponent getComponent() {
        return component;
    }

    public static MyApplication get(@NonNull Context context) {
        return (MyApplication) context.getApplicationContext();
    }
}