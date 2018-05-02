package com.timotiusoktorio.pencake.ui.orderdetail;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.timotiusoktorio.pencake.data.model.StoreInfo;
import com.timotiusoktorio.pencake.data.source.DataManager;

class OrderDetailViewModel extends ViewModel {

    final MutableLiveData<StoreInfo> storeInfoLiveData = new MutableLiveData<>();

    private final DataManager dataManager;

    OrderDetailViewModel(DataManager dataManager) {
        this.dataManager = dataManager;
        loadStoreInfo();
    }

    private void loadStoreInfo() {
        dataManager.fetchStoreInfo(new DataManager.SingleCallback<StoreInfo>() {
            @Override
            public void onSuccess(StoreInfo data) {
                storeInfoLiveData.setValue(data);
            }

            @Override
            public void onError(String errorMsg) {

            }
        });
    }
}