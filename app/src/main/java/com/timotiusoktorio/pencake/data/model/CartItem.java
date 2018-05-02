package com.timotiusoktorio.pencake.data.model;

import com.google.gson.Gson;

@SuppressWarnings("unused")
public class CartItem {

    private String id;
    private String productJson;
    private Integer selectedSizeIndex;
    private Integer quantity;
    private Integer subtotal;
    private String specialRequests;

    public CartItem() {
    }

    public CartItem(String id, String productJson, Integer selectedSizeIndex, Integer quantity, Integer subtotal, String specialRequests) {
        this.id = id;
        this.productJson = productJson;
        this.selectedSizeIndex = selectedSizeIndex;
        this.quantity = quantity;
        this.subtotal = subtotal;
        this.specialRequests = specialRequests;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProductJson() {
        return productJson;
    }

    public void setProductJson(String productJson) {
        this.productJson = productJson;
    }

    public Integer getSelectedSizeIndex() {
        return selectedSizeIndex;
    }

    public void setSelectedSizeIndex(Integer selectedSizeIndex) {
        this.selectedSizeIndex = selectedSizeIndex;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(Integer subtotal) {
        this.subtotal = subtotal;
    }

    public String getSpecialRequests() {
        return specialRequests;
    }

    public void setSpecialRequests(String specialRequests) {
        this.specialRequests = specialRequests;
    }

    @Override
    public String toString() {
        return toJson();
    }

    public String toJson() {
        return new Gson().toJson(this);
    }

    public static CartItem fromJson(String json) {
        return new Gson().fromJson(json, CartItem.class);
    }
}