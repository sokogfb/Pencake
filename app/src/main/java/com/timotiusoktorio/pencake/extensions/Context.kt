package com.timotiusoktorio.pencake.extensions

inline fun consume(func: () -> Unit): Boolean {
    func()
    return true
}