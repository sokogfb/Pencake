package com.timotiusoktorio.pencake.data.source;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.timotiusoktorio.pencake.data.model.CartItem;
import com.timotiusoktorio.pencake.data.model.Category;
import com.timotiusoktorio.pencake.data.model.ContactInfo;
import com.timotiusoktorio.pencake.data.model.Order;
import com.timotiusoktorio.pencake.data.model.Product;
import com.timotiusoktorio.pencake.data.model.StoreInfo;
import com.timotiusoktorio.pencake.data.model.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import timber.log.Timber;

@Singleton
public class AppDataManager implements DataManager {

    private PreferencesHelper preferencesHelper;

    @Inject
    public AppDataManager(PreferencesHelper preferencesHelper) {
        this.preferencesHelper = preferencesHelper;
    }

    @Override
    public void fetchCategories(final Callback<Category> callback) {
        FirebaseDatabase.getInstance().getReference()
                .child("categories").orderByChild("index")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot == null) {
                            callback.onError("No categories found");
                            return;
                        }
                        List<Category> categories = new ArrayList<>();
                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                            Category category = child.getValue(Category.class);
                            categories.add(category);
                        }
                        if (!categories.isEmpty()) callback.onSuccess(categories);
                        else callback.onError("No categories found");
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        if (databaseError != null) {
                            Timber.e(databaseError.toException());
                            callback.onError(databaseError.getMessage());
                        }
                    }
                });
    }

    @Override
    public void fetchProducts(String categoryId, final Callback<Product> callback) {
        FirebaseDatabase.getInstance().getReference()
                .child("products/" + categoryId).orderByChild("index")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot == null) {
                            callback.onError("No products found");
                            return;
                        }
                        List<Product> products = new ArrayList<>();
                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                            Product product = child.getValue(Product.class);
                            products.add(product);
                        }
                        if (!products.isEmpty()) callback.onSuccess(products);
                        else callback.onError("No products found");
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        if (databaseError != null) {
                            Timber.e(databaseError.toException());
                            callback.onError(databaseError.getMessage());
                        }
                    }
                });
    }

    @Override
    public void fetchCart(final Callback<CartItem> callback) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            return;
        }
        FirebaseDatabase.getInstance().getReference().child("cart/" + user.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot == null) {
                            callback.onError("No items found");
                            return;
                        }
                        List<CartItem> cartItems = new ArrayList<>();
                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                            CartItem cartItem = child.getValue(CartItem.class);
                            cartItems.add(cartItem);
                        }
                        if (!cartItems.isEmpty()) callback.onSuccess(cartItems);
                        else callback.onError("No items found");
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        if (databaseError != null) {
                            Timber.e(databaseError.toException());
                            callback.onError(databaseError.getMessage());
                        }
                    }
                });
    }

    @Override
    public void fetchStoreInfo(final SingleCallback<StoreInfo> callback) {
        FirebaseDatabase.getInstance().getReference().child("store")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot == null) {
                            callback.onError("Unable to load store info");
                            return;
                        }
                        StoreInfo storeInfo = dataSnapshot.getValue(StoreInfo.class);
                        if (storeInfo != null) callback.onSuccess(storeInfo);
                        else callback.onError("Unable to load store info");
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        if (databaseError != null) {
                            Timber.e(databaseError.toException());
                            callback.onError(databaseError.getMessage());
                        }
                    }
                });
    }

    @Override
    public void fetchOrders(boolean completed, final Callback<Order> callback) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            return;
        }
        FirebaseDatabase.getInstance().getReference().child("orders/" + user.getUid())
                .orderByChild("completed").equalTo(completed)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot == null) {
                            callback.onError("No orders found");
                            return;
                        }
                        List<Order> orders = new ArrayList<>();
                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                            Order order = child.getValue(Order.class);
                            orders.add(order);
                        }
                        if (!orders.isEmpty()) callback.onSuccess(orders);
                        else callback.onError("No orders found");
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        if (databaseError != null) {
                            Timber.e(databaseError.toException());
                            callback.onError(databaseError.getMessage());
                        }
                    }
                });
    }

    @Override
    public void fetchFavorites(final Callback<Product> callback) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            db.getReference().child("favorites/" + user.getUid())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot == null) {
                                callback.onError("No products found");
                                return;
                            }
                            List<Product> products = new ArrayList<>();
                            for (DataSnapshot child : dataSnapshot.getChildren()) {
                                Product product = child.getValue(Product.class);
                                products.add(product);
                            }
                            if (!products.isEmpty()) callback.onSuccess(products);
                            else callback.onError("No products found");
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            if (databaseError != null) {
                                Timber.e(databaseError.toException());
                                callback.onError(databaseError.getMessage());
                            }
                        }
                    });
        }
    }

    @Override
    public void fetchFavorite(String productId, final SingleCallback<Product> callback) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            db.getReference().child("favorites/" + user.getUid()).child(productId)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot == null) {
                                callback.onError("Unable to find product from database");
                                return;
                            }
                            Product product = dataSnapshot.getValue(Product.class);
                            if (product != null) callback.onSuccess(product);
                            else callback.onError("Unable to find product from database");
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            if (databaseError != null) {
                                Timber.e(databaseError.toException());
                                callback.onError(databaseError.getMessage());
                            }
                        }
                    });
        }
    }

    @Override
    public void fetchUser(final SingleCallback<User> callback) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
        if (fUser != null) {
            db.getReference().child("users/" + fUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot == null) {
                        callback.onError("Unable to find user from database");
                        return;
                    }
                    User user = dataSnapshot.getValue(User.class);
                    if (user != null) callback.onSuccess(user);
                    else callback.onError("Unable to find user from database");
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    if (databaseError != null) {
                        Timber.e(databaseError.toException());
                        callback.onError(databaseError.getMessage());
                    }
                }
            });
        }
    }

    @Override
    public void addCartItem(CartItem cartItem) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            DatabaseReference cartItemRef = db.getReference().child("cart").child(user.getUid()).push();
            cartItem.setId(cartItemRef.getKey());
            cartItemRef.setValue(cartItem);
        }
    }

    @Override
    public void updateCartItem(CartItem cartItem) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            db.getReference().child("cart/" + user.getUid() + "/" + cartItem.getId()).setValue(cartItem);
        }
    }

    @Override
    public void deleteCartItem(CartItem cartItem) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            db.getReference().child("cart/" + user.getUid() + "/" + cartItem.getId()).removeValue();
        }
    }

    @Override
    public void deleteCart() {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            db.getReference().child("cart/" + user.getUid()).removeValue();
            preferencesHelper.removeOrderContactInfo();
            preferencesHelper.removeOrderPickupSchedule();
        }
    }

    @Override
    public void addOrder(Order order) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            DatabaseReference orderRef = db.getReference().child("orders").child(user.getUid()).push();
            order.setId(orderRef.getKey());
            orderRef.setValue(order);
            deleteCart();
        }
    }

    @Override
    public void addFavorite(Product product) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            db.getReference().child("favorites").child(user.getUid()).child(product.getId()).setValue(product);
            updateFavoriteWidgetData();
        }
    }

    @Override
    public void deleteFavorite(Product product) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            db.getReference().child("favorites").child(user.getUid()).child(product.getId()).removeValue();
            updateFavoriteWidgetData();
        }
    }

    private void updateFavoriteWidgetData() {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            db.getReference().child("favorites/" + user.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot != null) {
                        List<Product> products = new ArrayList<>();
                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                            Product product = child.getValue(Product.class);
                            products.add(product);
                        }
                        preferencesHelper.saveFavoriteList(products);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    if (databaseError != null) {
                        Timber.e(databaseError.toException());
                    }
                }
            });
        }
    }

    @Override
    public void saveUser() {
        final FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
        if (fUser != null) {
            final FirebaseDatabase db = FirebaseDatabase.getInstance();
            db.getReference().child("users").child(fUser.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    User user;
                    if (dataSnapshot != null) {
                        user = dataSnapshot.getValue(User.class);
                        if (user == null) {
                            user = new User(fUser.getDisplayName(), fUser.getEmail(), "");
                            db.getReference().child("users").child(fUser.getUid()).setValue(user);
                        }
                    } else {
                        user = new User(fUser.getDisplayName(), fUser.getEmail(), "");
                        db.getReference().child("users").child(fUser.getUid()).setValue(user);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    if (databaseError != null) {
                        Timber.e(databaseError.toException());
                    }
                }
            });
        }
    }

    @Override
    public void updateUser(User user) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
        if (fUser != null) {
            db.getReference().child("users").child(fUser.getUid()).setValue(user);
        }
    }

    @Override
    public void updatePassword(String oldPassword, final String newPassword, final SingleCallback<Boolean> callback) {
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null && user.getEmail() != null) {
            AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), oldPassword);
            user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        // user is successfully re-authenticated. Proceed with password update.
                        user.updatePassword(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    callback.onSuccess(Boolean.TRUE);
                                } else {
                                    callback.onError("Update password failed. Please try again later");
                                }
                            }
                        });
                    } else {
                        callback.onError("Failed to re-authenticate user");
                    }
                }
            });
        }
    }

    @NonNull
    @Override
    public ContactInfo getOrderContactInfo() {
        return preferencesHelper.getOrderContactInfo();
    }

    @Override
    public void saveOrderContactInfo(ContactInfo contactInfo) {
        preferencesHelper.saveOrderContactInfo(contactInfo);
    }

    @Nullable
    @Override
    public Date getOrderPickupSchedule() {
        return preferencesHelper.getOrderPickupSchedule();
    }

    @Override
    public void saveOrderPickupSchedule(Date date) {
        preferencesHelper.saveOrderPickupSchedule(date);
    }

    @Override
    public void removeFavoriteList() {
        preferencesHelper.removeFavoriteList();
    }

    @Override
    public boolean getFavoritesUpdatedFlag() {
        return preferencesHelper.getFavoritesUpdatedFlag();
    }

    @Override
    public void setFavoritesUpdatedFlag(boolean flag) {
        preferencesHelper.setFavoritesUpdatedFlag(flag);
    }
}