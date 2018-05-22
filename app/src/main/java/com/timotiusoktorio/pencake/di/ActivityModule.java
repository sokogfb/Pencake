package com.timotiusoktorio.pencake.di;

import com.timotiusoktorio.pencake.data.source.DataManager;
import com.timotiusoktorio.pencake.ui.BaseActivity;
import com.timotiusoktorio.pencake.ui.profile.ProfileViewModelFactory;

import dagger.Module;
import dagger.Provides;

@Module
public class ActivityModule {

    private final BaseActivity activity;

    public ActivityModule(BaseActivity activity) {
        this.activity = activity;
    }

    @Provides
    public ProfileViewModelFactory provideProfileViewModelFactory(DataManager dataManager) {
        return new ProfileViewModelFactory(dataManager);
    }
}