package com.timotiusoktorio.pencake.ui.addtocart;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.timotiusoktorio.pencake.data.source.DataManager;

public class AddToCartViewModelFactory implements ViewModelProvider.Factory {

    private final DataManager dataManager;

    public AddToCartViewModelFactory(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new AddToCartViewModel(dataManager);
    }
}