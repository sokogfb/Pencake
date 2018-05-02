package com.timotiusoktorio.pencake.di;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.timotiusoktorio.pencake.MyApplication;
import com.timotiusoktorio.pencake.data.source.AppDataManager;
import com.timotiusoktorio.pencake.data.source.DataManager;
import com.timotiusoktorio.pencake.data.source.PreferencesHelper;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

    private final MyApplication application;

    public AppModule(MyApplication application) {
        this.application = application;
    }

    @Provides
    public Application provideApplication() {
        return application;
    }

    @Provides
    @Singleton
    public SharedPreferences provideSharedPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(application);
    }

    @Provides
    @Singleton
    public PreferencesHelper providePreferencesHelper(SharedPreferences sharedPreferences) {
        return new PreferencesHelper(sharedPreferences);
    }

    @Provides
    @Singleton
    public DataManager provideDataManager(AppDataManager appDataManager) {
        return appDataManager;
    }
}