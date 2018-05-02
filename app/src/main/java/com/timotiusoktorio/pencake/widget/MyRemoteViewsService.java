package com.timotiusoktorio.pencake.widget;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.timotiusoktorio.pencake.data.model.Product;
import com.timotiusoktorio.pencake.data.source.PreferencesHelper;
import com.timotiusoktorio.pencake.ui.productdetail.ProductDetailActivity;

import java.util.ArrayList;
import java.util.List;

public class MyRemoteViewsService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ListRemoteViewsFactory(this);
    }

    public class ListRemoteViewsFactory implements RemoteViewsFactory {

        private final Context context;
        private final List<Product> data = new ArrayList<>();

        ListRemoteViewsFactory(Context context) {
            this.context = context;
        }

        private void initData() {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
            PreferencesHelper preferencesHelper = new PreferencesHelper(sharedPreferences);
            List<Product> products = preferencesHelper.getFavoriteList();
            if (products != null) {
                data.clear();
                data.addAll(products);
            }
        }

        @Override
        public void onCreate() {
            initData();
        }

        @Override
        public void onDataSetChanged() {
            initData();
        }

        @Override
        public void onDestroy() {
            data.clear();
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public RemoteViews getViewAt(int position) {
            RemoteViews rv = new RemoteViews(context.getPackageName(), android.R.layout.simple_list_item_1);
            rv.setTextViewText(android.R.id.text1, data.get(position).getName());

            Intent fillInIntent = new Intent();
            fillInIntent.putExtra(ProductDetailActivity.EXTRA_PRODUCT_JSON, data.get(position).toJson());
            rv.setOnClickFillInIntent(android.R.id.text1, fillInIntent);
            return rv;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }
}