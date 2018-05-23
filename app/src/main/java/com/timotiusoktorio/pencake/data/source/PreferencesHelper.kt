package com.timotiusoktorio.pencake.data.source

import android.content.SharedPreferences
import com.timotiusoktorio.pencake.data.model.ContactInfo
import java.util.*

class PreferencesHelper(private val sharedPreferences: SharedPreferences) {

    fun getOrderContactInfo(): ContactInfo {
        val name = sharedPreferences.getString(KEY_CONTACT_NAME, "")
        val email = sharedPreferences.getString(KEY_CONTACT_EMAIL, "")
        val phone = sharedPreferences.getString(KEY_CONTACT_PHONE, "")
        return ContactInfo(name, email, phone)
    }

    fun saveOrderContactInfo(contactInfo: ContactInfo) {
        sharedPreferences.edit()
                .putString(KEY_CONTACT_NAME, contactInfo.name)
                .putString(KEY_CONTACT_EMAIL, contactInfo.email)
                .putString(KEY_CONTACT_PHONE, contactInfo.phone)
                .apply()
    }

    fun removeOrderContactInfo() {
        sharedPreferences.edit()
                .remove(KEY_CONTACT_NAME)
                .remove(KEY_CONTACT_EMAIL)
                .remove(KEY_CONTACT_PHONE)
                .apply()
    }

    fun getOrderPickupSchedule(): Date? {
        val dateInMillis = sharedPreferences.getLong(KEY_PICKUP_SCHEDULE_DATE, 0)
        return if (dateInMillis > 0) Date(dateInMillis) else null
    }

    fun saveOrderPickupSchedule(date: Date) {
        sharedPreferences.edit().putLong(KEY_PICKUP_SCHEDULE_DATE, date.time).apply()
    }

    fun removeOrderPickupSchedule() {
        sharedPreferences.edit().remove(KEY_PICKUP_SCHEDULE_DATE).apply()
    }

    fun getFavoritesUpdatedFlag(): Boolean {
        return sharedPreferences.getBoolean(KEY_FAVORITES_UPDATED_FLAG, false)
    }

    fun saveFavoritesUpdatedFlag(flag: Boolean) {
        sharedPreferences.edit().putBoolean(KEY_FAVORITES_UPDATED_FLAG, flag).apply()
    }

    companion object {

        private const val KEY_CONTACT_NAME = "key_contact_name"
        private const val KEY_CONTACT_EMAIL = "key_contact_email"
        private const val KEY_CONTACT_PHONE = "key_contact_phone"
        private const val KEY_PICKUP_SCHEDULE_DATE = "key_pickup_schedule_date"
        private const val KEY_FAVORITES_UPDATED_FLAG = "key_favorites_updated_flag"
    }
}