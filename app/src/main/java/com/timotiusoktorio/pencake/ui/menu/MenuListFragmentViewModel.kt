package com.timotiusoktorio.pencake.ui.menu

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.timotiusoktorio.pencake.data.model.Product
import com.timotiusoktorio.pencake.data.model.State
import com.timotiusoktorio.pencake.data.source.DataManager

class MenuListFragmentViewModel(private val dataManager: DataManager, private val categoryId: String) : ViewModel() {

    val stateLiveData = MutableLiveData<State>()
    val productsLiveData = MutableLiveData<List<Product>>()

    init {
        loadProducts()
    }

    private fun loadProducts() {
        stateLiveData.value = State.LOADING
        dataManager.fetchProducts(categoryId, object : DataManager.Callback<Product> {
            override fun onSuccess(data: List<Product>) {
                productsLiveData.value = data
                stateLiveData.value = State.SUCCESS
            }

            override fun onError(errorMsg: String) {
                stateLiveData.value = State.ERROR
            }
        })
    }
}