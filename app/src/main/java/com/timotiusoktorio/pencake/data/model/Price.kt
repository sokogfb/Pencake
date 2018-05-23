package com.timotiusoktorio.pencake.data.model

data class Price(var size: String = "", var price: Int = 0) {

    val priceAndSize: String
        get() = "$ $price ($size)"

    val sizeAndPrice: String
        get() = "$size - $ $price"
}