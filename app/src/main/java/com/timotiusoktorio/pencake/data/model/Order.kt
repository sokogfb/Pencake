package com.timotiusoktorio.pencake.data.model

import com.google.gson.Gson

data class Order(var id: String = "",
                 var orderDate: Long = 0,
                 var cart: List<CartItem> = emptyList(),
                 var subtotal: Int = 0,
                 var contactInfo: ContactInfo = ContactInfo(),
                 var pickupSchedule: Long = 0,
                 var pickupReadyDate: Long = 0,
                 var orderStatus: OrderStatus = OrderStatus.ORDER_PLACED,
                 var completed: Boolean = false) {

    override fun toString(): String = toJson()

    fun toJson(): String = Gson().toJson(this)

    companion object {

        fun fromJson(json: String?): Order? = Gson().fromJson(json, Order::class.java)
    }
}