package com.timotiusoktorio.pencake.extensions

import android.arch.lifecycle.*
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity

inline fun <reified T : ViewModel> FragmentActivity.getViewModel(crossinline factory: () -> T): T {
    val vmFactory = object : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <U : ViewModel> create(modelClass: Class<U>): U = factory() as U
    }
    return ViewModelProviders.of(this, vmFactory)[T::class.java]
}

inline fun <reified T : ViewModel> FragmentActivity.withViewModel(crossinline factory: () -> T, body: T.() -> Unit): T {
    val viewModel = getViewModel(factory)
    viewModel.body()
    return viewModel
}

inline fun <reified T : ViewModel> Fragment.getViewModel(crossinline factory: () -> T): T {
    val vmFactory = object : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <U : ViewModel> create(modelClass: Class<U>): U = factory() as U
    }
    return ViewModelProviders.of(this, vmFactory)[T::class.java]
}

inline fun <reified T : ViewModel> Fragment.withViewModel(crossinline factory: () -> T, body: T.() -> Unit): T {
    val viewModel = getViewModel(factory)
    viewModel.body()
    return viewModel
}

fun <T : Any, L : LiveData<T>> LifecycleOwner.observe(liveData: L, body: (T?) -> Unit) {
    liveData.observe(this, Observer(body))
}