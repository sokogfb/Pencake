package com.timotiusoktorio.pencake.data.model;

import com.google.gson.Gson;

import java.util.List;

@SuppressWarnings("unused")
public class Order {

    private String id;
    private Long orderDate;
    private List<CartItem> cart;
    private Integer subtotal;
    private ContactInfo contactInfo;
    private Long pickupSchedule;
    private Long pickupReadyDate;
    private OrderStatus orderStatus;
    private Boolean completed;

    public Order() {
    }

    public Order(String id, Long orderDate, List<CartItem> cart, Integer subtotal, ContactInfo contactInfo, Long pickupSchedule, Long pickupReadyDate, OrderStatus orderStatus, Boolean completed) {
        this.id = id;
        this.orderDate = orderDate;
        this.cart = cart;
        this.subtotal = subtotal;
        this.contactInfo = contactInfo;
        this.pickupSchedule = pickupSchedule;
        this.pickupReadyDate = pickupReadyDate;
        this.orderStatus = orderStatus;
        this.completed = completed;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Long orderDate) {
        this.orderDate = orderDate;
    }

    public List<CartItem> getCart() {
        return cart;
    }

    public void setCart(List<CartItem> cart) {
        this.cart = cart;
    }

    public Integer getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(Integer subtotal) {
        this.subtotal = subtotal;
    }

    public ContactInfo getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(ContactInfo contactInfo) {
        this.contactInfo = contactInfo;
    }

    public Long getPickupSchedule() {
        return pickupSchedule;
    }

    public void setPickupSchedule(Long pickupSchedule) {
        this.pickupSchedule = pickupSchedule;
    }

    public Long getPickupReadyDate() {
        return pickupReadyDate;
    }

    public void setPickupReadyDate(Long pickupReadyDate) {
        this.pickupReadyDate = pickupReadyDate;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Boolean getCompleted() {
        return completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }

    @Override
    public String toString() {
        return toJson();
    }

    public String toJson() {
        return new Gson().toJson(this);
    }

    public static Order fromJson(String json) {
        return new Gson().fromJson(json, Order.class);
    }
}