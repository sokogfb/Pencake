package com.timotiusoktorio.pencake.ui.addtocart

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.timotiusoktorio.pencake.data.model.CartItem
import com.timotiusoktorio.pencake.data.model.Product
import com.timotiusoktorio.pencake.data.source.DataManager

class AddToCartViewModel(private val dataManager: DataManager,
                         private val product: Product,
                         private val cartItem: CartItem?) : ViewModel() {

    val quantityLiveData = MutableLiveData<Int>()
    val subtotalLiveData = MutableLiveData<Int>()
    val exitFlagLiveData = MutableLiveData<Boolean>()

    private var selectedSizeIndex = 0
    private var specialRequests = ""

    init {
        if (cartItem == null) {
            setSelectedSizeIndex(0)
            setQuantity(1)
        } else {
            setSelectedSizeIndex(cartItem.selectedSizeIndex)
            setQuantity(cartItem.quantity)
        }
    }

    fun setSelectedSizeIndex(index: Int) {
        selectedSizeIndex = index
        calculateSubtotal()
    }

    fun setQuantity(quantity: Int) {
        quantityLiveData.value = quantity
        calculateSubtotal()
    }

    private fun calculateSubtotal() {
        quantityLiveData.value?.let {
            val price = product.prices[selectedSizeIndex].price
            subtotalLiveData.value = price * it
        }
    }

    fun setSpecialRequests(specialRequests: String) {
        this.specialRequests = specialRequests
    }

    fun decreaseQuantity() {
        quantityLiveData.value?.let { if (it > 1) setQuantity(it - 1) }
    }

    fun increaseQuantity() {
        quantityLiveData.value?.let { setQuantity(it + 1) }
    }

    fun addToCart() {
        if (cartItem == null) createCartItem()
        else updateCartItem(cartItem)
        exitFlagLiveData.value = true
    }

    private fun createCartItem() {
        val newCartItem = CartItem(
                productJson = product.toJson(),
                selectedSizeIndex = selectedSizeIndex,
                quantity = quantityLiveData.value ?: 0,
                subtotal = subtotalLiveData.value ?: 0,
                specialRequests = specialRequests
        )
        dataManager.addCartItem(newCartItem)
    }

    private fun updateCartItem(cartItem: CartItem) {
        cartItem.selectedSizeIndex = selectedSizeIndex
        cartItem.quantity = quantityLiveData.value ?: cartItem.quantity
        cartItem.subtotal = subtotalLiveData.value ?: cartItem.subtotal
        cartItem.specialRequests = specialRequests
        dataManager.updateCartItem(cartItem)
    }

    fun removeFromCart() {
        cartItem?.let {
            dataManager.deleteCartItem(it)
            exitFlagLiveData.value = true
        }
    }
}