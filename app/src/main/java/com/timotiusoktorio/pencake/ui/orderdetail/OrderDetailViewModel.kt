package com.timotiusoktorio.pencake.ui.orderdetail

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.timotiusoktorio.pencake.data.model.StoreInfo
import com.timotiusoktorio.pencake.data.source.DataManager

class OrderDetailViewModel(private val dataManager: DataManager) : ViewModel() {

    val storeInfoLiveData = MutableLiveData<StoreInfo>()

    init {
        loadStoreInfo()
    }

    private fun loadStoreInfo() {
        dataManager.fetchStoreInfo(object : DataManager.SingleCallback<StoreInfo> {
            override fun onSuccess(data: StoreInfo) {
                storeInfoLiveData.value = data
            }

            override fun onError(errorMsg: String) {
                // Not implemented
            }
        })
    }
}