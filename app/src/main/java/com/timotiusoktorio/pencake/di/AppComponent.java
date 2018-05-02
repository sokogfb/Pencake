package com.timotiusoktorio.pencake.di;

import android.app.Application;

import com.timotiusoktorio.pencake.data.source.DataManager;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = AppModule.class)
public interface AppComponent {

    Application getApplication();

    DataManager getDataManager();
}