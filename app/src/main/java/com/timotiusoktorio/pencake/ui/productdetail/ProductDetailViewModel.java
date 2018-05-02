package com.timotiusoktorio.pencake.ui.productdetail;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.timotiusoktorio.pencake.data.model.CartItem;
import com.timotiusoktorio.pencake.data.model.Product;
import com.timotiusoktorio.pencake.data.source.DataManager;

import java.util.List;

class ProductDetailViewModel extends ViewModel {

    final MutableLiveData<Integer> cartQuantityLiveData = new MutableLiveData<>();
    final MutableLiveData<Integer> cartSubtotalLiveData = new MutableLiveData<>();
    final MutableLiveData<Boolean> favoriteFlagLiveData = new MutableLiveData<>();

    private DataManager dataManager;
    private Product product;

    ProductDetailViewModel(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    void setProduct(Product product) {
        this.product = product;
    }

    void loadCart() {
        dataManager.fetchCart(new DataManager.Callback<CartItem>() {
            @Override
            public void onSuccess(List<CartItem> data) {
                int quantity = 0;
                int subtotal = 0;
                for (CartItem cartItem : data) {
                    quantity += cartItem.getQuantity();
                    subtotal += cartItem.getSubtotal();
                }
                cartQuantityLiveData.setValue(quantity);
                cartSubtotalLiveData.setValue(subtotal);
            }

            @Override
            public void onError(String errorMsg) {
                cartQuantityLiveData.setValue(0);
                cartSubtotalLiveData.setValue(0);
            }
        });
    }

    void checkFavorite() {
        dataManager.fetchFavorite(product.getId(), new DataManager.SingleCallback<Product>() {
            @Override
            public void onSuccess(Product data) {
                favoriteFlagLiveData.setValue(true);
            }

            @Override
            public void onError(String errorMsg) {
                favoriteFlagLiveData.setValue(false);
            }
        });
    }

    void updateFavorite() {
        Boolean favoriteFlag = favoriteFlagLiveData.getValue();
        if (favoriteFlag != null) {
            favoriteFlagLiveData.setValue(!favoriteFlag);
            if (favoriteFlag) dataManager.deleteFavorite(product);
            else dataManager.addFavorite(product);
            dataManager.setFavoritesUpdatedFlag(true);
        }
    }
}