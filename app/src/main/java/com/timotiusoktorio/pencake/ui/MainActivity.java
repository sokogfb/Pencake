package com.timotiusoktorio.pencake.ui;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.timotiusoktorio.pencake.R;
import com.timotiusoktorio.pencake.data.source.DataManager;
import com.timotiusoktorio.pencake.databinding.ActivityMainBinding;
import com.timotiusoktorio.pencake.ui.favorites.FavoritesFragment;
import com.timotiusoktorio.pencake.ui.menu.MenuFragment;
import com.timotiusoktorio.pencake.ui.orders.OrdersFragment;
import com.timotiusoktorio.pencake.ui.profile.ProfileFragment;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static final String ACTION_SIGN_OUT = "com.timotiusoktorio.pencake.action.SIGN_OUT";
    private static final String EXTRA_NAV_MENU_ITEM_INDEX = "EXTRA_NAV_MENU_ITEM_INDEX";
    private static final int RC_SIGN_IN = 100;

    @Inject DataManager dataManager;

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        getComponent().inject(this);
        setSupportActionBar(binding.toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, binding.drawerLayout, binding.toolbar, R.string.content_desc_open_drawer, R.string.content_desc_close_drawer);
        binding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        binding.navigationView.setNavigationItemSelectedListener(this);

        boolean userIsLoggedIn = FirebaseAuth.getInstance().getCurrentUser() != null;
        if (userIsLoggedIn) selectNavMenuItem(0);
        else goToSignInScreen();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        int navMenuItemIndex = intent.getIntExtra(EXTRA_NAV_MENU_ITEM_INDEX, -1);
        if (navMenuItemIndex != -1) {
            selectNavMenuItem(navMenuItemIndex);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == Activity.RESULT_OK) {
                dataManager.saveUser();
                selectNavMenuItem(0);
            } else if (resultCode == Activity.RESULT_CANCELED) {
                finish();
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter(ACTION_SIGN_OUT);
        LocalBroadcastManager.getInstance(this).registerReceiver(signOutBroadcastReceiver, intentFilter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(signOutBroadcastReceiver);
    }

    @Override
    public void onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_menu: {
                replaceFragment(MenuFragment.newInstance());
                binding.drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
            case R.id.nav_orders: {
                replaceFragment(OrdersFragment.newInstance());
                binding.drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
            case R.id.nav_favorites: {
                replaceFragment(FavoritesFragment.newInstance());
                binding.drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
            case R.id.nav_profile: {
                replaceFragment(ProfileFragment.newInstance());
                binding.drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
            default: {
                binding.drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        }
    }

    private void selectNavMenuItem(int menuItemIndex) {
        MenuItem menuItem = binding.navigationView.getMenu().getItem(menuItemIndex);
        binding.navigationView.setCheckedItem(menuItem.getItemId());
        onNavigationItemSelected(menuItem);
    }

    private void goToSignInScreen() {
        List<AuthUI.IdpConfig> providers = Collections.singletonList(new AuthUI.IdpConfig.EmailBuilder().build());
        startActivityForResult(AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .setLogo(R.drawable.app_logo)
                .build(), RC_SIGN_IN);
    }

    private void replaceFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .commit();
    }

    private BroadcastReceiver signOutBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action != null && action.equals(ACTION_SIGN_OUT)) {
                goToSignInScreen();
            }
        }
    };

    public static Intent newIntent(FragmentActivity activity, int navMenuItemIndex) {
        Intent intent = new Intent(activity, MainActivity.class);
        intent.putExtra(EXTRA_NAV_MENU_ITEM_INDEX, navMenuItemIndex);
        return intent;
    }
}