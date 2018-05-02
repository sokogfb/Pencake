package com.timotiusoktorio.pencake;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;

import com.timotiusoktorio.pencake.data.model.Product;
import com.timotiusoktorio.pencake.data.source.AppDataManager;
import com.timotiusoktorio.pencake.data.source.DataManager;
import com.timotiusoktorio.pencake.data.source.PreferencesHelper;
import com.timotiusoktorio.pencake.widget.MyAppWidgetProvider;

import java.util.List;

import timber.log.Timber;

/**
 * This service is responsible for fetching initial favorites data from Firebase Realtime Database and
 * save it to SharedPreferences. This is necessary because the app widget fetches favorites data from
 * SharedPreferences. Upon first launch, SharedPreferences will be empty and it needs to be filled with
 * data from Realtime Database. This service should be started as early as possible.
 */
public class FavoritesDataService extends IntentService {

    public FavoritesDataService() {
        super("FavoritesDataService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Timber.d("onHandleIntent");
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        final PreferencesHelper preferencesHelper = new PreferencesHelper(sharedPreferences);
        DataManager dataManager = new AppDataManager(preferencesHelper);
        dataManager.fetchFavorites(new DataManager.Callback<Product>() {
            @Override
            public void onSuccess(List<Product> data) {
                preferencesHelper.saveFavoriteList(data);
                updateAppWidgetProvider();
            }

            @Override
            public void onError(String errorMsg) {

            }
        });
    }

    private void updateAppWidgetProvider() {
        Intent intent = new Intent(this, MyAppWidgetProvider.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        ComponentName componentName = new ComponentName(getApplication(), MyAppWidgetProvider.class);
        int[] appWidgetI‌​ds = AppWidgetManager.getInstance(getApplication()).getAppWidgetIds(componentName);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetI‌​ds);
        sendBroadcast(intent);
    }
}