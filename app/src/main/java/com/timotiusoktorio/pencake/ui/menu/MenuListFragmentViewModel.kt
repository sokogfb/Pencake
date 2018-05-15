package com.timotiusoktorio.pencake.ui.menu

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.timotiusoktorio.pencake.data.model.KState
import com.timotiusoktorio.pencake.data.model.Product
import com.timotiusoktorio.pencake.data.source.DataManager

class MenuListFragmentViewModel(private val categoryId: String, val dataManager: DataManager) : ViewModel() {

    val productsLiveData: MutableLiveData<List<Product>> = MutableLiveData()
    val stateLiveData: MutableLiveData<KState> = MutableLiveData()

    init {
        loadProducts()
    }

    private fun loadProducts() {
        stateLiveData.value = KState.LOADING
        dataManager.fetchProducts(categoryId, object : DataManager.Callback<Product> {
            override fun onSuccess(data: MutableList<Product>?) {
                productsLiveData.value = data
                stateLiveData.value = KState.SUCCESS
            }

            override fun onError(errorMsg: String?) {
                stateLiveData.value = KState.ERROR
            }
        })
    }
}