package com.timotiusoktorio.pencake

import android.app.Application
import android.content.Context

import com.timotiusoktorio.pencake.di.AppComponent
import com.timotiusoktorio.pencake.di.AppModule
import com.timotiusoktorio.pencake.di.DaggerAppComponent

import timber.log.Timber

class MyApplication : Application() {

    lateinit var component: AppComponent

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        component = DaggerAppComponent.builder()
                .appModule(AppModule(this))
                .build()
    }

    companion object {

        operator fun get(context: Context): MyApplication = context.applicationContext as MyApplication
    }
}