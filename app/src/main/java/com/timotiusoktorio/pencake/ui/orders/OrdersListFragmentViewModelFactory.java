package com.timotiusoktorio.pencake.ui.orders;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.timotiusoktorio.pencake.data.source.DataManager;

public class OrdersListFragmentViewModelFactory implements ViewModelProvider.Factory {

    private final DataManager dataManager;

    public OrdersListFragmentViewModelFactory(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new OrdersListFragmentViewModel(dataManager);
    }
}