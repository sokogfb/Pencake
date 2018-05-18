package com.timotiusoktorio.pencake.extensions

import java.text.SimpleDateFormat
import java.util.*

fun Date.toShortDateFormat(): String = SimpleDateFormat("MMM dd", Locale.CANADA).format(this)

fun Date.toMediumDateFormat(): String = SimpleDateFormat("EEEE, MMMM d", Locale.CANADA).format(this)

fun Date.toLongDateFormat(): String = SimpleDateFormat("MMMM dd, YYYY", Locale.CANADA).format(this)

fun Date.toTimeFormat(): String = SimpleDateFormat("h:mm a", Locale.CANADA).format(this)