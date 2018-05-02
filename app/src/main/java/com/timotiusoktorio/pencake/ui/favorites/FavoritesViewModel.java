package com.timotiusoktorio.pencake.ui.favorites;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.timotiusoktorio.pencake.data.model.Product;
import com.timotiusoktorio.pencake.data.model.State;
import com.timotiusoktorio.pencake.data.source.DataManager;

import java.util.List;

class FavoritesViewModel extends ViewModel {

    final MutableLiveData<State> stateLiveData = new MutableLiveData<>();
    final MutableLiveData<List<Product>> productsLiveData = new MutableLiveData<>();

    private final DataManager dataManager;

    FavoritesViewModel(DataManager dataManager) {
        this.dataManager = dataManager;
        loadFavorites();
    }

    void refreshFavorites() {
        if (dataManager.getFavoritesUpdatedFlag()) {
            dataManager.setFavoritesUpdatedFlag(false);
            loadFavorites();
        }
    }

    private void loadFavorites() {
        stateLiveData.setValue(State.LOADING);
        dataManager.fetchFavorites(new DataManager.Callback<Product>() {
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