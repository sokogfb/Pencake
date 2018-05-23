package com.timotiusoktorio.pencake.data.source

import com.timotiusoktorio.pencake.data.model.*
import java.util.*

interface DataManager {

    // GET OPERATIONS

    fun fetchCategories(callback: Callback<Category>)

    fun fetchProducts(categoryId: String, callback: Callback<Product>)

    fun fetchCart(callback: Callback<CartItem>)

    fun fetchStoreInfo(callback: SingleCallback<StoreInfo>)

    fun fetchOrders(completed: Boolean, callback: Callback<Order>)

    fun fetchFavorites(callback: Callback<Product>)

    fun fetchFavorite(productId: String, callback: SingleCallback<Product>)

    fun fetchUser(callback: SingleCallback<User>)

    // POST OPERATIONS

    fun addCartItem(cartItem: CartItem)

    fun updateCartItem(cartItem: CartItem)

    fun deleteCartItem(cartItem: CartItem)

    fun deleteCart()

    fun addOrder(order: Order)

    fun addFavorite(product: Product)

    fun deleteFavorite(product: Product)

    fun saveUser()

    fun updateUser(user: User)

    fun updatePassword(oldPassword: String, newPassword: String, callback: SingleCallback<Boolean>)

    // SHARED PREFERENCES OPERATIONS

    fun getOrderContactInfo(): ContactInfo

    fun saveOrderContactInfo(contactInfo: ContactInfo)

    fun getOrderPickupSchedule(): Date?

    fun saveOrderPickupSchedule(date: Date)

    fun getFavoritesUpdatedFlag(): Boolean

    fun saveFavoritesUpdatedFlag(flag: Boolean)

    // CALLBACKS

    interface Callback<T> {

        fun onSuccess(data: List<T>)

        fun onError(errorMsg: String)
    }

    interface SingleCallback<T> {

        fun onSuccess(data: T)

        fun onError(errorMsg: String)
    }
}