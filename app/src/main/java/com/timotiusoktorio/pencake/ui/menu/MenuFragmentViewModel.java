package com.timotiusoktorio.pencake.ui.menu;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.timotiusoktorio.pencake.data.model.Category;
import com.timotiusoktorio.pencake.data.model.State;
import com.timotiusoktorio.pencake.data.source.DataManager;

import java.util.List;

class MenuFragmentViewModel extends ViewModel {

    final MutableLiveData<List<Category>> categoriesLiveData = new MutableLiveData<>();
    final MutableLiveData<State> stateLiveData = new MutableLiveData<>();

    private final DataManager dataManager;

    MenuFragmentViewModel(DataManager dataManager) {
        this.dataManager = dataManager;
        loadCategories();
    }

    private void loadCategories() {
        stateLiveData.setValue(State.LOADING);
        dataManager.fetchCategories(new DataManager.Callback<Category>() {
            @Override
            public void onSuccess(List<Category> data) {
                categoriesLiveData.setValue(data);
                stateLiveData.setValue(State.SUCCESS);
            }

            @Override
            public void onError(String errorMsg) {
                stateLiveData.setValue(State.ERROR);
            }
        });
    }
}