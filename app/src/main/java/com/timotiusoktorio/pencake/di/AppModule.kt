package com.timotiusoktorio.pencake.di

import android.app.Application
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.timotiusoktorio.pencake.MyApplication
import com.timotiusoktorio.pencake.data.source.AppDataManager
import com.timotiusoktorio.pencake.data.source.DataManager
import com.timotiusoktorio.pencake.data.source.PreferencesHelper
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(private val application: MyApplication) {

    @Provides
    fun provideApplication(): Application {
        return application
    }

    @Provides
    @Singleton
    fun provideSharedPreferences(): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(application)
    }

    @Provides
    @Singleton
    fun providePreferencesHelper(sharedPreferences: SharedPreferences): PreferencesHelper {
        return PreferencesHelper(sharedPreferences)
    }

    @Provides
    @Singleton
    fun provideDataManager(appDataManager: AppDataManager): DataManager {
        return appDataManager
    }
}