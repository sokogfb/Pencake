package com.timotiusoktorio.pencake.ui.productdetail

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.timotiusoktorio.pencake.data.model.CartItem
import com.timotiusoktorio.pencake.data.model.Product
import com.timotiusoktorio.pencake.data.source.DataManager

class ProductDetailViewModel(private val dataManager: DataManager, private val product: Product) : ViewModel() {

    val cartQuantityLiveData = MutableLiveData<Int>()
    val cartSubtotalLiveData = MutableLiveData<Int>()
    val favoriteFlagLiveData = MutableLiveData<Boolean>()

    fun loadCart() {
        dataManager.fetchCart(object : DataManager.Callback<CartItem> {
            override fun onSuccess(data: List<CartItem>) {
                var quantity = 0
                var subtotal = 0
                for (cartItem in data) {
                    quantity += cartItem.quantity
                    subtotal += cartItem.subtotal
                }
                cartQuantityLiveData.value = quantity
                cartSubtotalLiveData.value = subtotal
            }

            override fun onError(errorMsg: String) {
                cartQuantityLiveData.value = 0
                cartSubtotalLiveData.value = 0
            }
        })
    }

    fun checkFavorite() {
        dataManager.fetchFavorite(product.id, object : DataManager.SingleCallback<Product> {
            override fun onSuccess(data: Product) {
                favoriteFlagLiveData.value = true
            }

            override fun onError(errorMsg: String) {
                favoriteFlagLiveData.value = false
            }
        })
    }

    fun updateFavorite() {
        favoriteFlagLiveData.value?.let {
            favoriteFlagLiveData.value = !it
            if (it) dataManager.deleteFavorite(product)
            else dataManager.addFavorite(product)
            dataManager.saveFavoritesUpdatedFlag(true)
        }
    }
}