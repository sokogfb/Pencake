package com.timotiusoktorio.pencake.data.model;

import com.timotiusoktorio.pencake.R;

@SuppressWarnings("unused")
public enum OrderStatus {

    ORDER_PLACED(0, R.string.text_order_placed),
    PREPARING(1, R.string.text_preparing),
    PICKUP_READY(2, R.string.text_pickup_ready);

    private final int id;
    private final int titleRes;

    OrderStatus(int id, int titleRes) {
        this.id = id;
        this.titleRes = titleRes;
    }

    public int getId() {
        return id;
    }

    public int getTitleRes() {
        return titleRes;
    }
}