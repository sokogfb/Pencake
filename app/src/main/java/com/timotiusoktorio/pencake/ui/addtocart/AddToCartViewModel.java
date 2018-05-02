package com.timotiusoktorio.pencake.ui.addtocart;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.Nullable;

import com.timotiusoktorio.pencake.data.model.CartItem;
import com.timotiusoktorio.pencake.data.model.Product;
import com.timotiusoktorio.pencake.data.source.DataManager;

class AddToCartViewModel extends ViewModel {

    final MutableLiveData<Integer> quantityLiveData = new MutableLiveData<>();
    final MutableLiveData<Integer> subtotalLiveData = new MutableLiveData<>();
    final MutableLiveData<Boolean> exitFlagLiveData = new MutableLiveData<>();

    private DataManager dataManager;
    private Product product;
    private CartItem cartItem;
    private Integer selectedSizeIndex;

    AddToCartViewModel(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    void setInitialSizeAndQuantity(Product product, @Nullable CartItem cartItem) {
        this.product = product;
        this.cartItem = cartItem;

        if (cartItem == null) {
            setSelectedSizeIndex(0);
            setQuantity(1);
        } else {
            setSelectedSizeIndex(cartItem.getSelectedSizeIndex());
            setQuantity(cartItem.getQuantity());
        }
    }

    void setSelectedSizeIndex(Integer index) {
        selectedSizeIndex = index;
        setSubtotal();
    }

    void setQuantity(Integer quantity) {
        quantityLiveData.setValue(quantity);
        setSubtotal();
    }

    private void setSubtotal() {
        Integer quantity = quantityLiveData.getValue();
        if (quantity == null) {
            return;
        }
        Integer price = product.getPrices().get(selectedSizeIndex).getPrice();
        subtotalLiveData.setValue(price * quantity);
    }

    void onMinusButtonClick() {
        Integer quantity = quantityLiveData.getValue();
        if (quantity != null) {
            if (quantity > 1) {
                quantity--;
                setQuantity(quantity);
            }
        }
    }

    void onPlusButtonClick() {
        Integer quantity = quantityLiveData.getValue();
        if (quantity != null) {
            quantity++;
            setQuantity(quantity);
        }
    }

    void onSaveCartClick(String specialRequests) {
        if (cartItem == null) {
            CartItem newCartItem = new CartItem(null, product.toJson(), selectedSizeIndex, quantityLiveData.getValue(), subtotalLiveData.getValue(), specialRequests);
            dataManager.addCartItem(newCartItem);
        } else {
            cartItem.setSelectedSizeIndex(selectedSizeIndex);
            cartItem.setQuantity(quantityLiveData.getValue());
            cartItem.setSubtotal(subtotalLiveData.getValue());
            cartItem.setSpecialRequests(specialRequests);
            dataManager.updateCartItem(cartItem);
        }
        exitFlagLiveData.setValue(true);
    }

    void removeFromCart() {
        if (cartItem != null) {
            dataManager.deleteCartItem(cartItem);
            exitFlagLiveData.setValue(true);
        }
    }

    boolean isUpdatingCart() {
        return cartItem != null;
    }
}