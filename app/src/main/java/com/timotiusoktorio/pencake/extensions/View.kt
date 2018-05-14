package com.timotiusoktorio.pencake.extensions

import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

inline fun DrawerLayout.consume(func: () -> Unit): Boolean {
    func()
    closeDrawer(GravityCompat.START)
    return true
}

fun DrawerLayout.isOpen(): Boolean = isDrawerOpen(GravityCompat.START)

fun DrawerLayout.close() = closeDrawer(GravityCompat.START)

fun ViewGroup.inflate(layoutRes: Int): View = LayoutInflater.from(context).inflate(layoutRes, this, false)