package com.timotiusoktorio.pencake.data.model

import com.google.gson.Gson

data class CartItem(var id: String = "",
                    var productJson: String = "",
                    var selectedSizeIndex: Int = 0,
                    var quantity: Int = 0,
                    var subtotal: Int = 0,
                    var specialRequests: String = "") {

    override fun toString(): String = toJson()

    fun toJson(): String = Gson().toJson(this)

    companion object {

        fun fromJson(json: String?): CartItem? = Gson().fromJson(json, CartItem::class.java)
    }
}