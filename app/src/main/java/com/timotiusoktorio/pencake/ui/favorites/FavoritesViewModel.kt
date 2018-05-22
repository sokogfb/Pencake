package com.timotiusoktorio.pencake.ui.favorites

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.timotiusoktorio.pencake.data.model.KState
import com.timotiusoktorio.pencake.data.model.Product
import com.timotiusoktorio.pencake.data.source.DataManager

class FavoritesViewModel(private val dataManager: DataManager) : ViewModel() {

    val stateLiveData = MutableLiveData<KState>()
    val productsLiveData = MutableLiveData<List<Product>>()

    init {
        loadFavorites()
    }

    private fun loadFavorites() {
        stateLiveData.value = KState.LOADING
        dataManager.fetchFavorites(object : DataManager.Callback<Product> {
            override fun onSuccess(data: MutableList<Product>) {
                productsLiveData.value = data
                stateLiveData.value = KState.SUCCESS
            }

            override fun onError(errorMsg: String) {
                stateLiveData.value = KState.ERROR
            }
        })
    }

    fun refreshFavorites() {
        if (dataManager.favoritesUpdatedFlag) {
            dataManager.favoritesUpdatedFlag = false
            loadFavorites()
        }
    }
}