package com.timotiusoktorio.pencake.data.model

import com.google.gson.Gson
import java.util.*

data class Product(var id: String = "",
                   var name: String = "",
                   var description: String = "",
                   var prices: List<Price> = emptyList(),
                   var imageUrls: List<String> = emptyList(),
                   var timeOfWork: Int = 0,
                   var timeOfWorkDesc: String = "") {

    val combinedPrices: String
        get() {
            val sb = StringBuilder()
            for (i in prices.indices) {
                sb.append(prices[i].priceAndSize)
                if (i != prices.size - 1) {
                    sb.append("\n")
                }
            }
            return sb.toString()
        }

    val sizes: List<String>
        get() {
            val sizes = ArrayList<String>()
            for (price in prices) {
                sizes.add(price.sizeAndPrice)
            }
            return sizes
        }

    override fun toString(): String = toJson()

    fun toJson(): String = Gson().toJson(this)

    companion object {

        fun fromJson(json: String?): Product? = Gson().fromJson(json, Product::class.java)
    }
}