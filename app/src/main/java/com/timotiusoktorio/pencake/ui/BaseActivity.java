package com.timotiusoktorio.pencake.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.timotiusoktorio.pencake.MyApplication;
import com.timotiusoktorio.pencake.di.ActivityComponent;
import com.timotiusoktorio.pencake.di.ActivityModule;
import com.timotiusoktorio.pencake.di.DaggerActivityComponent;

public abstract class BaseActivity extends AppCompatActivity {

    private ActivityComponent component;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        component = DaggerActivityComponent.builder()
                .appComponent(MyApplication.get(this).getComponent())
                .activityModule(new ActivityModule(this))
                .build();
    }

    public ActivityComponent getComponent() {
        return component;
    }
}