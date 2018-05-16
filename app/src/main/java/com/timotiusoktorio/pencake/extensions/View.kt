package com.timotiusoktorio.pencake.extensions

import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import com.squareup.picasso.Picasso
import com.timotiusoktorio.pencake.R

inline fun DrawerLayout.consume(func: () -> Unit): Boolean {
    func()
    closeDrawer(GravityCompat.START)
    return true
}

fun DrawerLayout.isOpen(): Boolean = isDrawerOpen(GravityCompat.START)

fun DrawerLayout.close() = closeDrawer(GravityCompat.START)

fun EditText.afterTextChanged(func: (Editable) -> Unit) {
    addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            s?.let(func)
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

        }
    })
}

fun ImageView.loadUrl(url: String) = Picasso.get().load(url).fit().centerCrop()
        .placeholder(R.drawable.placeholder_image)
        .into(this)

fun ViewGroup.inflate(layoutRes: Int): View = LayoutInflater.from(context).inflate(layoutRes, this, false)