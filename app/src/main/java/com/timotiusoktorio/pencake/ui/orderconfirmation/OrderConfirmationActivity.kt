package com.timotiusoktorio.pencake.ui.orderconfirmation

import android.os.Bundle
import com.timotiusoktorio.pencake.R
import com.timotiusoktorio.pencake.ui.BaseActivity
import com.timotiusoktorio.pencake.ui.MainActivity
import kotlinx.android.synthetic.main.activity_order_confirmation.*
import org.jetbrains.anko.startActivity

class OrderConfirmationActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_confirmation)
        viewOrdersBtn.setOnClickListener { startActivity<MainActivity>(MainActivity.EXTRA_NAV_MENU_ITEM_INDEX to 1) }
        backToMenuBtn.setOnClickListener { startActivity<MainActivity>(MainActivity.EXTRA_NAV_MENU_ITEM_INDEX to 0) }
    }
}