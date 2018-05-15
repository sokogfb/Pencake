package com.timotiusoktorio.pencake.ui

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.app.ActionBarDrawerToggle
import android.view.MenuItem
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.timotiusoktorio.pencake.R
import com.timotiusoktorio.pencake.data.source.DataManager
import com.timotiusoktorio.pencake.extensions.close
import com.timotiusoktorio.pencake.extensions.consume
import com.timotiusoktorio.pencake.extensions.isOpen
import com.timotiusoktorio.pencake.extensions.replaceFragment
import com.timotiusoktorio.pencake.ui.favorites.FavoritesFragment
import com.timotiusoktorio.pencake.ui.menu.MenuFragment
import com.timotiusoktorio.pencake.ui.orders.OrdersFragment
import com.timotiusoktorio.pencake.ui.profile.ProfileFragment
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.toast
import java.util.*
import javax.inject.Inject

class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {

    @Inject lateinit var dataManager: DataManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        component.inject(this)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.content_desc_open_drawer, R.string.content_desc_close_drawer)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navigationView.setNavigationItemSelectedListener(this)

        val userIsSignedIn = (FirebaseAuth.getInstance().currentUser != null)
        if (userIsSignedIn) selectNavMenuItem(0)
        else goToSignInScreen()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        val navMenuItemIndex = intent?.getIntExtra(EXTRA_NAV_MENU_ITEM_INDEX, -1) ?: -1
        if (navMenuItemIndex != -1) {
            selectNavMenuItem(navMenuItemIndex)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == Activity.RESULT_OK) {
                dataManager.saveUser()
                selectNavMenuItem(0)
            } else if (resultCode == Activity.RESULT_CANCELED) {
                finish()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        LocalBroadcastManager.getInstance(this).registerReceiver(signOutBroadcastReceiver, IntentFilter(ACTION_SIGN_OUT))
    }

    override fun onStop() {
        super.onStop()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(signOutBroadcastReceiver)
    }

    override fun onBackPressed() {
        if (drawerLayout.isOpen()) drawerLayout.close()
        else super.onBackPressed()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.nav_menu -> drawerLayout.consume { replaceFragment(R.id.fragment, MenuFragment.newInstance()) }
        R.id.nav_orders -> drawerLayout.consume { replaceFragment(R.id.fragment, OrdersFragment.newInstance()) }
        R.id.nav_favorites -> drawerLayout.consume { replaceFragment(R.id.fragment, FavoritesFragment.newInstance()) }
        R.id.nav_profile -> drawerLayout.consume { replaceFragment(R.id.fragment, ProfileFragment.newInstance()) }
        else -> drawerLayout.consume { toast("Unsupported MenuItem") }
    }

    private fun selectNavMenuItem(menuItemIndex: Int) {
        val menuItem = navigationView.menu.getItem(menuItemIndex)
        navigationView.setCheckedItem(menuItem.itemId)
        onNavigationItemSelected(menuItem)
    }

    private fun goToSignInScreen() {
        val providers = Collections.singletonList(AuthUI.IdpConfig.EmailBuilder().build())
        startActivityForResult(AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .setLogo(R.drawable.app_logo)
                .build(), RC_SIGN_IN)
    }

    private val signOutBroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            intent?.action?.let {
                if (it == ACTION_SIGN_OUT) {
                    goToSignInScreen()
                }
            }
        }
    }

    companion object {

        const val ACTION_SIGN_OUT = "com.timotiusoktorio.pencake.action.SIGN_OUT"
        const val EXTRA_NAV_MENU_ITEM_INDEX = "EXTRA_NAV_MENU_ITEM_INDEX"
        const val RC_SIGN_IN = 100
    }
}