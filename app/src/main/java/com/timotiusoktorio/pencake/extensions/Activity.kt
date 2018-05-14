package com.timotiusoktorio.pencake.extensions

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity

private inline fun FragmentManager.transaction(func: FragmentTransaction.() -> FragmentTransaction) =
        beginTransaction().func().commit()

fun AppCompatActivity.addFragment(containerViewId: Int, fragment: Fragment) {
    supportFragmentManager.transaction { add(containerViewId, fragment) }
}

fun AppCompatActivity.replaceFragment(containerViewId: Int, fragment: Fragment) {
    supportFragmentManager.transaction { replace(containerViewId, fragment) }
}