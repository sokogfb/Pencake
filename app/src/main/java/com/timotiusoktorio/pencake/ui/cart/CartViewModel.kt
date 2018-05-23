package com.timotiusoktorio.pencake.ui.cart

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.timotiusoktorio.pencake.data.model.*
import com.timotiusoktorio.pencake.data.source.DataManager
import java.util.*

class CartViewModel(private val dataManager: DataManager) : ViewModel() {

    val stateLiveData = MutableLiveData<State>()
    val cartLiveData = MutableLiveData<List<CartItem>>()
    val quantityLiveData = MutableLiveData<Int>()
    val subtotalLiveData = MutableLiveData<Int>()
    val contactInfoLiveData = MutableLiveData<ContactInfo>()
    val pickupScheduleLiveData = MutableLiveData<Calendar>()
    val storeInfoLiveData = MutableLiveData<StoreInfo>()
    val exitFlagLiveData = MutableLiveData<Boolean>()
    val placeOrderErrorFlagLiveData = MutableLiveData<Boolean>()

    init {
        loadCart()
        loadContactInfo()
        loadPickupSchedule()
        loadStoreInfo()
    }

    fun loadCart() {
        stateLiveData.value = State.LOADING
        dataManager.fetchCart(object : DataManager.Callback<CartItem> {
            override fun onSuccess(data: MutableList<CartItem>) {
                cartLiveData.value = data
                calculateQuantityAndSubtotal(data)
                stateLiveData.value = State.SUCCESS
            }

            override fun onError(errorMsg: String) {
                stateLiveData.value = State.ERROR
            }
        })
    }

    private fun calculateQuantityAndSubtotal(cart: List<CartItem>) {
        var quantity = 0
        var subtotal = 0
        for (cartItem in cart) {
            quantity += cartItem.quantity
            subtotal += cartItem.subtotal
        }
        quantityLiveData.value = quantity
        subtotalLiveData.value = subtotal
    }

    private fun loadContactInfo() {
        val contactInfo = dataManager.orderContactInfo
        contactInfoLiveData.value = contactInfo
    }

    private fun loadPickupSchedule() {
        dataManager.orderPickupSchedule?.let {
            val calendar = Calendar.getInstance()
            calendar.time = it
            pickupScheduleLiveData.value = calendar
        }
    }

    private fun loadStoreInfo() {
        dataManager.fetchStoreInfo(object : DataManager.SingleCallback<StoreInfo> {
            override fun onSuccess(data: StoreInfo) {
                storeInfoLiveData.value = data
            }

            override fun onError(errorMsg: String) {

            }
        })
    }

    fun saveContactInfo(name: String, email: String, phone: String) {
        dataManager.saveOrderContactInfo(ContactInfo(name, email, phone))
    }

    fun onDateSet(year: Int, month: Int, dayOfMonth: Int) {
        val pickupSchedule: Calendar = pickupScheduleLiveData.value ?: Calendar.getInstance()
        pickupSchedule.set(year, month, dayOfMonth)
        savePickupSchedule(pickupSchedule)
    }

    fun onTimeSet(hourOfDay: Int, minute: Int) {
        val pickupSchedule: Calendar = pickupScheduleLiveData.value ?: Calendar.getInstance()
        val year = pickupSchedule.get(Calendar.YEAR)
        val month = pickupSchedule.get(Calendar.MONTH)
        val dayOfMonth = pickupSchedule.get(Calendar.DAY_OF_MONTH)
        pickupSchedule.set(year, month, dayOfMonth, hourOfDay, minute)
        savePickupSchedule(pickupSchedule)
    }

    private fun savePickupSchedule(pickupSchedule: Calendar) {
        pickupScheduleLiveData.value = pickupSchedule
        dataManager.saveOrderPickupSchedule(pickupSchedule.time)
    }

    fun clearCart() {
        dataManager.deleteCart()
        loadCart()
    }

    fun placeOrder(name: String, email: String, phone: String, date: String, time: String) {
        if (name.isBlank() && email.isBlank() && phone.isBlank() && date.isBlank() && time.isBlank()) {
            placeOrderErrorFlagLiveData.value = true
            return
        }
        val order = Order(
                orderDate = Date().time,
                cart = cartLiveData.value ?: emptyList(),
                subtotal = subtotalLiveData.value ?: 0,
                contactInfo = ContactInfo(name, email, phone),
                pickupSchedule = pickupScheduleLiveData.value?.time?.time ?: 0
        )
        dataManager.addOrder(order)
        exitFlagLiveData.value = true
    }
}