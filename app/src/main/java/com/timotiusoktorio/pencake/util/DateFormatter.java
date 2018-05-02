package com.timotiusoktorio.pencake.util;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public final class DateFormatter {

    @NonNull
    public static String formatShortDate(@Nullable Long dateMillis) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd", Locale.CANADA);
        return dateMillis != null ? dateFormat.format(new Date(dateMillis)) : "";
    }

    @NonNull
    public static String formatLongDate(@Nullable Calendar calendar) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, MMMM d", Locale.CANADA);
        return calendar != null ? dateFormat.format(calendar.getTime()) : "";
    }

    @NonNull
    public static String formatLongerDate(@Nullable Long dateMillis) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, YYYY", Locale.CANADA);
        return dateMillis != null ? dateFormat.format(new Date(dateMillis)) : "";
    }

    @NonNull
    public static String formatTime(@Nullable Calendar calendar) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a", Locale.CANADA);
        return calendar != null ? timeFormat.format(calendar.getTime()) : "";
    }
}