package com.timotiusoktorio.pencake.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.timotiusoktorio.pencake.R;
import com.timotiusoktorio.pencake.ui.MainActivity;
import com.timotiusoktorio.pencake.ui.productdetail.ProductDetailActivity;

public class MyAppWidgetProvider extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        updateAllAppWidgets(context, appWidgetManager, appWidgetIds);
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    private static void updateAllAppWidgets(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.layout_app_widget);
            rv.setRemoteAdapter(R.id.listView, new Intent(context, MyRemoteViewsService.class));

            Intent mainActivityIntent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, mainActivityIntent, 0);
            rv.setOnClickPendingIntent(R.id.rootContainer, pendingIntent);

            Intent productDetailIntent = new Intent(context, ProductDetailActivity.class);
            PendingIntent productDetailPIntent = PendingIntent.getActivity(context, 0, productDetailIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            rv.setPendingIntentTemplate(R.id.listView, productDetailPIntent);

            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.listView);
            appWidgetManager.updateAppWidget(appWidgetId, rv);
        }
    }
}