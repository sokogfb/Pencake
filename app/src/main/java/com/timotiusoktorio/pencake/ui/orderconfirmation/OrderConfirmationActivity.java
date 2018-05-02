package com.timotiusoktorio.pencake.ui.orderconfirmation;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.timotiusoktorio.pencake.R;
import com.timotiusoktorio.pencake.ui.BaseActivity;
import com.timotiusoktorio.pencake.ui.MainActivity;

public class OrderConfirmationActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_confirmation);
    }

    public void onViewOrdersButtonClick(View view) {
        startActivity(MainActivity.newIntent(this, 1));
    }

    public void onBackToMenuButtonClick(View view) {
        startActivity(MainActivity.newIntent(this, 0));
    }
}