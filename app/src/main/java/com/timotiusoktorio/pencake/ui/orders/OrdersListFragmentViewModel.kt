package com.timotiusoktorio.pencake.ui.orders

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.timotiusoktorio.pencake.data.model.Order
import com.timotiusoktorio.pencake.data.model.State
import com.timotiusoktorio.pencake.data.source.DataManager

class OrdersListFragmentViewModel(private val dataManager: DataManager, private val tabIndex: Int) : ViewModel() {

    val stateLiveData = MutableLiveData<State>()
    val ordersLiveData = MutableLiveData<List<Order>>()

    init {
        loadOrders()
    }

    private fun loadOrders() {
        stateLiveData.value = State.LOADING
        dataManager.fetchOrders(tabIndex == 1, object : DataManager.Callback<Order> {
            override fun onSuccess(data: List<Order>) {
                ordersLiveData.value = data
                stateLiveData.value = State.SUCCESS
            }

            override fun onError(errorMsg: String) {
                stateLiveData.value = State.ERROR
            }
        })
    }
}