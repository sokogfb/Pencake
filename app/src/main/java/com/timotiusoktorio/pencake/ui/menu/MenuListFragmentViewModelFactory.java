package com.timotiusoktorio.pencake.ui.menu;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.timotiusoktorio.pencake.data.source.DataManager;

public class MenuListFragmentViewModelFactory implements ViewModelProvider.Factory {

    private final DataManager dataManager;

    public MenuListFragmentViewModelFactory(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new MenuListFragmentViewModel(dataManager);
    }
}