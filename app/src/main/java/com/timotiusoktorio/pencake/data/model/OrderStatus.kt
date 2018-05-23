package com.timotiusoktorio.pencake.data.model

import com.timotiusoktorio.pencake.R

enum class OrderStatus(val id: Int, val titleRes: Int) {

    ORDER_PLACED(0, R.string.text_order_placed),
    PREPARING(1, R.string.text_preparing),
    PICKUP_READY(2, R.string.text_pickup_ready)
}