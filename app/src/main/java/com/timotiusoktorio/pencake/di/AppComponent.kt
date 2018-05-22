package com.timotiusoktorio.pencake.di

import android.app.Application
import com.timotiusoktorio.pencake.data.source.DataManager
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [(AppModule::class)])
interface AppComponent {

    val application: Application

    val dataManager: DataManager
}