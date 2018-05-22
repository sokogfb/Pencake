package com.timotiusoktorio.pencake.di;

import com.timotiusoktorio.pencake.ui.BaseActivity;

import dagger.Module;

@Module
public class ActivityModule {

    private final BaseActivity activity;

    public ActivityModule(BaseActivity activity) {
        this.activity = activity;
    }
}