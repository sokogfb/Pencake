package com.timotiusoktorio.pencake.ui.cart;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.timotiusoktorio.pencake.data.model.CartItem;
import com.timotiusoktorio.pencake.data.model.ContactInfo;
import com.timotiusoktorio.pencake.data.model.Order;
import com.timotiusoktorio.pencake.data.model.OrderStatus;
import com.timotiusoktorio.pencake.data.model.State;
import com.timotiusoktorio.pencake.data.model.StoreInfo;
import com.timotiusoktorio.pencake.data.source.DataManager;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

class CartViewModel extends ViewModel {

    final MutableLiveData<State> stateLiveData = new MutableLiveData<>();
    final MutableLiveData<List<CartItem>> cartLiveData = new MutableLiveData<>();
    final MutableLiveData<Integer> quantityLiveData = new MutableLiveData<>();
    final MutableLiveData<Integer> subtotalLiveData = new MutableLiveData<>();
    final MutableLiveData<ContactInfo> contactInfoLiveData = new MutableLiveData<>();
    final MutableLiveData<Calendar> pickupScheduleLiveData = new MutableLiveData<>();
    final MutableLiveData<StoreInfo> storeInfoLiveData = new MutableLiveData<>();
    final MutableLiveData<Boolean> exitFlagLiveData = new MutableLiveData<>();

    private final DataManager dataManager;

    CartViewModel(DataManager dataManager) {
        this.dataManager = dataManager;
        loadCart();
        loadContactInfo();
        loadPickupSchedule();
        loadStoreInfo();
    }

    void loadCart() {
        stateLiveData.setValue(State.LOADING);
        dataManager.fetchCart(new DataManager.Callback<CartItem>() {
            @Override
            public void onSuccess(List<CartItem> data) {
                cartLiveData.setValue(data);
                calculateQuantityAndSubtotal(data);
                stateLiveData.setValue(State.SUCCESS);
            }

            @Override
            public void onError(String errorMsg) {
                stateLiveData.setValue(State.ERROR);
            }
        });
    }

    private void calculateQuantityAndSubtotal(List<CartItem> cart) {
        int quantity = 0;
        int subtotal = 0;
        for (CartItem cartItem : cart) {
            quantity += cartItem.getQuantity();
            subtotal += cartItem.getSubtotal();
        }
        quantityLiveData.setValue(quantity);
        subtotalLiveData.setValue(subtotal);
    }

    private void loadContactInfo() {
        ContactInfo contactInfo = dataManager.getOrderContactInfo();
        contactInfoLiveData.setValue(contactInfo);
    }

    private void loadPickupSchedule() {
        Date pickupScheduleDate = dataManager.getOrderPickupSchedule();
        if (pickupScheduleDate != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(pickupScheduleDate);
            pickupScheduleLiveData.setValue(calendar);
        }
    }

    private void loadStoreInfo() {
        dataManager.fetchStoreInfo(new DataManager.SingleCallback<StoreInfo>() {
            @Override
            public void onSuccess(StoreInfo data) {
                storeInfoLiveData.setValue(data);
            }

            @Override
            public void onError(String errorMsg) {
            }
        });
    }

    void saveContactInfo(String name, String email, String phone) {
        ContactInfo contactInfo = new ContactInfo(name, email, phone);
        dataManager.saveOrderContactInfo(contactInfo);
    }

    void onDateSet(int year, int month, int dayOfMonth) {
        Calendar pickupSchedule = pickupScheduleLiveData.getValue();
        if (pickupSchedule == null) {
            pickupSchedule = Calendar.getInstance();
        }
        pickupSchedule.set(year, month, dayOfMonth);
        savePickupSchedule(pickupSchedule);
    }

    void onTimeSet(int hourOfDay, int minute) {
        Calendar pickupSchedule = pickupScheduleLiveData.getValue();
        if (pickupSchedule == null) {
            pickupSchedule = Calendar.getInstance();
        }
        int year = pickupSchedule.get(Calendar.YEAR);
        int month = pickupSchedule.get(Calendar.MONTH);
        int dayOfMonth = pickupSchedule.get(Calendar.DAY_OF_MONTH);
        pickupSchedule.set(year, month, dayOfMonth, hourOfDay, minute);
        savePickupSchedule(pickupSchedule);
    }

    private void savePickupSchedule(Calendar pickupSchedule) {
        pickupScheduleLiveData.setValue(pickupSchedule);
        dataManager.saveOrderPickupSchedule(pickupSchedule.getTime());
    }

    void clearCart() {
        dataManager.deleteCart();
        loadCart();
    }

    void placeOrder(ContactInfo contactInfo) {
        Long orderDate = new Date().getTime();
        List<CartItem> cart = cartLiveData.getValue();
        Integer orderSubtotal = subtotalLiveData.getValue();
        Long pickupSchedule = pickupScheduleLiveData.getValue() != null ? pickupScheduleLiveData.getValue().getTime().getTime() : 0;
        Order order = new Order(null, orderDate, cart, orderSubtotal, contactInfo, pickupSchedule, null, OrderStatus.ORDER_PLACED, false);
        dataManager.addOrder(order);
        exitFlagLiveData.setValue(true);
    }
}