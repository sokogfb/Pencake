package com.timotiusoktorio.pencake.ui.orders;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.timotiusoktorio.pencake.data.model.Order;
import com.timotiusoktorio.pencake.data.model.State;
import com.timotiusoktorio.pencake.data.source.DataManager;

import java.util.List;

class OrdersListFragmentViewModel extends ViewModel {

    final MutableLiveData<State> stateLiveData = new MutableLiveData<>();
    final MutableLiveData<List<Order>> ordersLiveData = new MutableLiveData<>();

    private final DataManager dataManager;

    OrdersListFragmentViewModel(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    void loadOrders(int tabIndex) {
        stateLiveData.setValue(State.LOADING);
        dataManager.fetchOrders(tabIndex == 1, new DataManager.Callback<Order>() {
            @Override
            public void onSuccess(List<Order> data) {
                ordersLiveData.setValue(data);
                stateLiveData.setValue(State.SUCCESS);
            }

            @Override
            public void onError(String errorMsg) {
                stateLiveData.setValue(State.ERROR);
            }
        });
    }
}