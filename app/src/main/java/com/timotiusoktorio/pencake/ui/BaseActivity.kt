package com.timotiusoktorio.pencake.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

import com.timotiusoktorio.pencake.MyApplication
import com.timotiusoktorio.pencake.di.ActivityComponent
import com.timotiusoktorio.pencake.di.ActivityModule
import com.timotiusoktorio.pencake.di.DaggerActivityComponent

abstract class BaseActivity : AppCompatActivity() {

    lateinit var component: ActivityComponent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component = DaggerActivityComponent.builder()
                .appComponent(MyApplication[this].component)
                .activityModule(ActivityModule(this))
                .build()
    }
}