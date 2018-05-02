package com.timotiusoktorio.pencake.ui.favorites;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.timotiusoktorio.pencake.data.source.DataManager;

public class FavoritesViewModelFactory implements ViewModelProvider.Factory {

    private final DataManager dataManager;

    public FavoritesViewModelFactory(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new FavoritesViewModel(dataManager);
    }
}