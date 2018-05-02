package com.timotiusoktorio.pencake.data.source;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.timotiusoktorio.pencake.data.model.ContactInfo;
import com.timotiusoktorio.pencake.data.model.Product;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;

public class PreferencesHelper {

    private final SharedPreferences sharedPreferences;

    public PreferencesHelper(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    @NonNull
    public ContactInfo getOrderContactInfo() {
        String name = sharedPreferences.getString("key_contact_name", "");
        String email = sharedPreferences.getString("key_contact_email", "");
        String phone = sharedPreferences.getString("key_contact_phone", "");
        return new ContactInfo(name, email, phone);
    }

    public void saveOrderContactInfo(ContactInfo contactInfo) {
        sharedPreferences.edit()
                .putString("key_contact_name", contactInfo.getName())
                .putString("key_contact_email", contactInfo.getEmail())
                .putString("key_contact_phone", contactInfo.getPhone())
                .apply();
    }

    public void removeOrderContactInfo() {
        sharedPreferences.edit()
                .remove("key_contact_name")
                .remove("key_contact_email")
                .remove("key_contact_phone")
                .apply();
    }

    @Nullable
    public Date getOrderPickupSchedule() {
        long dateInMillis = sharedPreferences.getLong("key_pickup_schedule_date", 0);
        if (dateInMillis > 0) {
            return new Date(dateInMillis);
        }
        return null;
    }

    public void saveOrderPickupSchedule(Date date) {
        sharedPreferences.edit().putLong("key_pickup_schedule_date", date.getTime()).apply();
    }

    public void removeOrderPickupSchedule() {
        sharedPreferences.edit().remove("key_pickup_schedule_date").apply();
    }

    public boolean getFavoritesUpdatedFlag() {
        return sharedPreferences.getBoolean("key_favorites_updated_flag", false);
    }

    public void setFavoritesUpdatedFlag(boolean flag) {
        sharedPreferences.edit().putBoolean("key_favorites_updated_flag", flag).apply();
    }

    public void saveFavoriteList(List<Product> products) {
        String jsonList = new Gson().toJson(products);
        sharedPreferences.edit().putString("key_favorite_list", jsonList).apply();
    }

    public void removeFavoriteList() {
        sharedPreferences.edit().remove("key_favorite_list").apply();
    }

    public List<Product> getFavoriteList() {
        String jsonList = sharedPreferences.getString("key_favorite_list", "");
        Type collectionType = new TypeToken<List<Product>>() {}.getType();
        return new Gson().fromJson(jsonList, collectionType);
    }
}