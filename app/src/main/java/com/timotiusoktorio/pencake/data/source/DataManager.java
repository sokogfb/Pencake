package com.timotiusoktorio.pencake.data.source;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.timotiusoktorio.pencake.data.model.CartItem;
import com.timotiusoktorio.pencake.data.model.Category;
import com.timotiusoktorio.pencake.data.model.ContactInfo;
import com.timotiusoktorio.pencake.data.model.Order;
import com.timotiusoktorio.pencake.data.model.Product;
import com.timotiusoktorio.pencake.data.model.StoreInfo;
import com.timotiusoktorio.pencake.data.model.User;

import java.util.Date;
import java.util.List;

public interface DataManager {

    // GET OPERATIONS

    void fetchCategories(Callback<Category> callback);

    void fetchProducts(String categoryId, Callback<Product> callback);

    void fetchCart(Callback<CartItem> callback);

    void fetchStoreInfo(SingleCallback<StoreInfo> callback);

    void fetchOrders(boolean completed, Callback<Order> callback);

    void fetchFavorites(Callback<Product> callback);

    void fetchFavorite(String productId, SingleCallback<Product> callback);

    void fetchUser(SingleCallback<User> callback);

    // POST OPERATIONS

    void addCartItem(CartItem cartItem);

    void updateCartItem(CartItem cartItem);

    void deleteCartItem(CartItem cartItem);

    void deleteCart();

    void addOrder(Order order);

    void addFavorite(Product product);

    void deleteFavorite(Product product);

    void saveUser();

    void updateUser(User user);

    void updatePassword(String oldPassword, String newPassword, SingleCallback<Boolean> callback);

    // SHARED PREFERENCES OPERATIONS

    @NonNull ContactInfo getOrderContactInfo();

    void saveOrderContactInfo(ContactInfo contactInfo);

    @Nullable Date getOrderPickupSchedule();

    void saveOrderPickupSchedule(Date date);

    void removeFavoriteList();

    boolean getFavoritesUpdatedFlag();

    void setFavoritesUpdatedFlag(boolean flag);

    interface Callback<T> {

        void onSuccess(List<T> data);

        void onError(String errorMsg);
    }

    interface SingleCallback<T> {

        void onSuccess(T data);

        void onError(String errorMsg);
    }
}