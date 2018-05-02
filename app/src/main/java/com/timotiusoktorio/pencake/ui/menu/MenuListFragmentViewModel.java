package com.timotiusoktorio.pencake.ui.menu;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.timotiusoktorio.pencake.data.model.Product;
import com.timotiusoktorio.pencake.data.model.State;
import com.timotiusoktorio.pencake.data.source.DataManager;

import java.util.List;

class MenuListFragmentViewModel extends ViewModel {

    final MutableLiveData<List<Product>> productsLiveData = new MutableLiveData<>();
    final MutableLiveData<State> stateLiveData = new MutableLiveData<>();

    private final DataManager dataManager;

    MenuListFragmentViewModel(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    void loadProducts(String categoryId) {
        stateLiveData.setValue(State.LOADING);
        dataManager.fetchProducts(categoryId, new DataManager.Callback<Product>() {
            @Override
            public void onSuccess(List<Product> data) {
                productsLiveData.setValue(data);
                stateLiveData.setValue(State.SUCCESS);
            }

            @Override
            public void onError(String errorMsg) {
                stateLiveData.setValue(State.ERROR);
            }
        });
    }
}