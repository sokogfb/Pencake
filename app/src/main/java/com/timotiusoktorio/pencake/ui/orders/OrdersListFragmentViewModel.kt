package com.timotiusoktorio.pencake.ui.orders

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.timotiusoktorio.pencake.data.model.KState
import com.timotiusoktorio.pencake.data.model.Order
import com.timotiusoktorio.pencake.data.source.DataManager

class OrdersListFragmentViewModel(private val dataManager: DataManager, private val tabIndex: Int) : ViewModel() {

    val stateLiveData = MutableLiveData<KState>()
    val ordersLiveData = MutableLiveData<List<Order>>()

    init {
        loadOrders()
    }

    private fun loadOrders() {
        stateLiveData.value = KState.LOADING
        dataManager.fetchOrders(tabIndex == 1, object : DataManager.Callback<Order> {
            override fun onSuccess(data: MutableList<Order>?) {
                ordersLiveData.value = data
                stateLiveData.value = KState.SUCCESS
            }

            override fun onError(errorMsg: String?) {
                stateLiveData.value = KState.ERROR
            }
        })
    }
}