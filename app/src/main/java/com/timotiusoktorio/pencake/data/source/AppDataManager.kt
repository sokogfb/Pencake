package com.timotiusoktorio.pencake.data.source

import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.timotiusoktorio.pencake.data.model.*
import timber.log.Timber
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppDataManager @Inject constructor(private val preferencesHelper: PreferencesHelper) : DataManager {

    override fun fetchCategories(callback: DataManager.Callback<Category>) {
        FirebaseDatabase.getInstance().reference.child("categories").orderByChild("index")
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot?) {
                        val categories = mutableListOf<Category>()
                        dataSnapshot?.let {
                            it.children.mapNotNullTo(categories) {
                                it.getValue(Category::class.java)
                            }
                        }
                        if (categories.isNotEmpty()) callback.onSuccess(categories)
                        else callback.onError("No categories found")
                    }

                    override fun onCancelled(databaseError: DatabaseError?) {
                        databaseError?.let {
                            Timber.e(it.toException())
                            callback.onError(it.message)
                        }
                    }
                })
    }

    override fun fetchProducts(categoryId: String, callback: DataManager.Callback<Product>) {
        FirebaseDatabase.getInstance().reference.child("products/$categoryId").orderByChild("index")
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot?) {
                        val products = mutableListOf<Product>()
                        dataSnapshot?.let {
                            it.children.mapNotNullTo(products) {
                                it.getValue(Product::class.java)
                            }
                        }
                        if (products.isNotEmpty()) callback.onSuccess(products)
                        else callback.onError("No products found")
                    }

                    override fun onCancelled(databaseError: DatabaseError?) {
                        databaseError?.let {
                            Timber.e(it.toException())
                            callback.onError(it.message)
                        }
                    }
                })
    }

    override fun fetchCart(callback: DataManager.Callback<CartItem>) {
        val user = FirebaseAuth.getInstance().currentUser ?: return
        FirebaseDatabase.getInstance().reference.child("cart/${user.uid}")
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot?) {
                        val cart = mutableListOf<CartItem>()
                        dataSnapshot?.let {
                            it.children.mapNotNullTo(cart) {
                                it.getValue(CartItem::class.java)
                            }
                        }
                        if (cart.isNotEmpty()) callback.onSuccess(cart)
                        else callback.onError("No items found")
                    }

                    override fun onCancelled(databaseError: DatabaseError?) {
                        databaseError?.let {
                            Timber.e(it.toException())
                            callback.onError(it.message)
                        }
                    }
                })
    }

    override fun fetchStoreInfo(callback: DataManager.SingleCallback<StoreInfo>) {
        FirebaseDatabase.getInstance().reference.child("store")
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot?) {
                        val storeInfo = dataSnapshot?.getValue(StoreInfo::class.java)
                        if (storeInfo != null) callback.onSuccess(storeInfo)
                        else callback.onError("Store info not found")
                    }

                    override fun onCancelled(databaseError: DatabaseError?) {
                        databaseError?.let {
                            Timber.e(it.toException())
                            callback.onError(it.message)
                        }
                    }
                })
    }

    override fun fetchOrders(completed: Boolean, callback: DataManager.Callback<Order>) {
        val user = FirebaseAuth.getInstance().currentUser ?: return
        FirebaseDatabase.getInstance().reference.child("orders/${user.uid}")
                .orderByChild("completed").equalTo(completed)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot?) {
                        val orders = mutableListOf<Order>()
                        dataSnapshot?.let {
                            it.children.mapNotNullTo(orders) {
                                it.getValue(Order::class.java)
                            }
                        }
                        if (orders.isNotEmpty()) callback.onSuccess(orders)
                        else callback.onError("No orders found")
                    }

                    override fun onCancelled(databaseError: DatabaseError?) {
                        databaseError?.let {
                            Timber.e(it.toException())
                            callback.onError(it.message)
                        }
                    }
                })
    }

    override fun fetchFavorites(callback: DataManager.Callback<Product>) {
        val user = FirebaseAuth.getInstance().currentUser ?: return
        FirebaseDatabase.getInstance().reference.child("favorites/${user.uid}")
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot?) {
                        val products = mutableListOf<Product>()
                        dataSnapshot?.let {
                            it.children.mapNotNullTo(products) {
                                it.getValue(Product::class.java)
                            }
                        }
                        if (products.isNotEmpty()) callback.onSuccess(products)
                        else callback.onError("No products found")
                    }

                    override fun onCancelled(databaseError: DatabaseError?) {
                        databaseError?.let {
                            Timber.e(it.toException())
                            callback.onError(it.message)
                        }
                    }
                })
    }

    override fun fetchFavorite(productId: String, callback: DataManager.SingleCallback<Product>) {
        val user = FirebaseAuth.getInstance().currentUser ?: return
        FirebaseDatabase.getInstance().reference.child("favorites/${user.uid}/$productId")
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot?) {
                        val product = dataSnapshot?.getValue(Product::class.java)
                        if (product != null) callback.onSuccess(product)
                        else callback.onError("Product not found")
                    }

                    override fun onCancelled(databaseError: DatabaseError?) {
                        databaseError?.let {
                            Timber.e(it.toException())
                            callback.onError(it.message)
                        }
                    }
                })
    }

    override fun fetchUser(callback: DataManager.SingleCallback<User>) {
        val fUser = FirebaseAuth.getInstance().currentUser ?: return
        FirebaseDatabase.getInstance().reference.child("users/${fUser.uid}")
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot?) {
                        val user = dataSnapshot?.getValue(User::class.java)
                        if (user != null) callback.onSuccess(user)
                        else callback.onError("User not found")
                    }

                    override fun onCancelled(databaseError: DatabaseError?) {
                        databaseError?.let {
                            Timber.e(it.toException())
                            callback.onError(it.message)
                        }
                    }
                })
    }

    override fun addCartItem(cartItem: CartItem) {
        val user = FirebaseAuth.getInstance().currentUser ?: return
        val cartItemRef = FirebaseDatabase.getInstance().reference.child("cart/${user.uid}").push()
        cartItem.id = cartItemRef.key
        cartItemRef.setValue(cartItem)
    }

    override fun updateCartItem(cartItem: CartItem) {
        val user = FirebaseAuth.getInstance().currentUser ?: return
        FirebaseDatabase.getInstance().reference.child("cart/${user.uid}/${cartItem.id}").setValue(cartItem)
    }

    override fun deleteCartItem(cartItem: CartItem) {
        val user = FirebaseAuth.getInstance().currentUser ?: return
        FirebaseDatabase.getInstance().reference.child("cart/${user.uid}/${cartItem.id}").removeValue()
    }

    override fun deleteCart() {
        val user = FirebaseAuth.getInstance().currentUser ?: return
        FirebaseDatabase.getInstance().reference.child("cart/${user.uid}").removeValue()
        preferencesHelper.removeOrderContactInfo()
        preferencesHelper.removeOrderPickupSchedule()
    }

    override fun addOrder(order: Order) {
        val user = FirebaseAuth.getInstance().currentUser ?: return
        val orderRef = FirebaseDatabase.getInstance().reference.child("orders/${user.uid}").push()
        order.id = orderRef.key
        orderRef.setValue(order)
        deleteCart()
    }

    override fun addFavorite(product: Product) {
        val user = FirebaseAuth.getInstance().currentUser ?: return
        FirebaseDatabase.getInstance().reference.child("favorites/${user.uid}/${product.id}").setValue(product)
    }

    override fun deleteFavorite(product: Product) {
        val user = FirebaseAuth.getInstance().currentUser ?: return
        FirebaseDatabase.getInstance().reference.child("favorites/${user.uid}/${product.id}").removeValue()
    }

    override fun saveUser() {
        val db = FirebaseDatabase.getInstance()
        val fUser = FirebaseAuth.getInstance().currentUser ?: return
        db.reference.child("users/${fUser.uid}").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot?) {
                var user = dataSnapshot?.getValue(User::class.java)
                if (user == null) {
                    user = User(fUser.displayName!!, fUser.email!!)
                    db.reference.child("users/${fUser.uid}").setValue(user)
                }
            }

            override fun onCancelled(databaseError: DatabaseError?) {
                Timber.e(databaseError?.toException())
            }
        })
    }

    override fun updateUser(user: User) {
        val fUser = FirebaseAuth.getInstance().currentUser ?: return
        FirebaseDatabase.getInstance().reference.child("users/${fUser.uid}").setValue(user)
    }

    override fun updatePassword(oldPassword: String, newPassword: String, callback: DataManager.SingleCallback<Boolean>) {
        val user = FirebaseAuth.getInstance().currentUser ?: return
        // Re-authenticate the user first.
        val credential = EmailAuthProvider.getCredential(user.email!!, oldPassword)
        user.reauthenticate(credential).addOnCompleteListener {
            if (it.isSuccessful) {
                // Re-authentication success. Proceed with password update.
                user.updatePassword(newPassword).addOnCompleteListener {
                    if (it.isSuccessful) callback.onSuccess(true)
                    else callback.onError("Update password failed. Please try again later")
                }
            } else {
                Timber.e(it.exception)
                callback.onError("Failed to re-authenticate user")
            }
        }
    }

    override fun getOrderContactInfo(): ContactInfo = preferencesHelper.getOrderContactInfo()

    override fun saveOrderContactInfo(contactInfo: ContactInfo) = preferencesHelper.saveOrderContactInfo(contactInfo)

    override fun getOrderPickupSchedule(): Date? = preferencesHelper.getOrderPickupSchedule()

    override fun saveOrderPickupSchedule(date: Date) = preferencesHelper.saveOrderPickupSchedule(date)

    override fun getFavoritesUpdatedFlag(): Boolean = preferencesHelper.getFavoritesUpdatedFlag()

    override fun saveFavoritesUpdatedFlag(flag: Boolean) = preferencesHelper.saveFavoritesUpdatedFlag(flag)
}