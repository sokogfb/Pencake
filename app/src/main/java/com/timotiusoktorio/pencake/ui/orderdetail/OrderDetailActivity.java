package com.timotiusoktorio.pencake.ui.orderdetail;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.timotiusoktorio.pencake.R;
import com.timotiusoktorio.pencake.data.model.Coordinate;
import com.timotiusoktorio.pencake.data.model.Order;
import com.timotiusoktorio.pencake.data.model.StoreInfo;
import com.timotiusoktorio.pencake.databinding.ActivityOrderDetailBinding;
import com.timotiusoktorio.pencake.ui.BaseActivity;
import com.timotiusoktorio.pencake.ui.adapters.CartRvAdapter;

import javax.inject.Inject;

public class OrderDetailActivity extends BaseActivity implements OnMapReadyCallback {

    private static final String EXTRA_ORDER_JSON = "EXTRA_ORDER_JSON";

    @Inject OrderDetailViewModelFactory vmFactory;

    private ActivityOrderDetailBinding binding;
    private OrderDetailViewModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_order_detail);
        getComponent().inject(this);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        viewModel = ViewModelProviders.of(this, vmFactory).get(OrderDetailViewModel.class);
        viewModel.storeInfoLiveData.observe(this, new Observer<StoreInfo>() {
            @Override
            public void onChanged(@Nullable StoreInfo storeInfo) {
                updateStoreInfo(storeInfo);
            }
        });

        String orderJson = getIntent().getStringExtra(EXTRA_ORDER_JSON);
        if (orderJson == null) {
            throw new IllegalStateException("Order JSON was not sent here as intent extra");
        }

        final Order order = Order.fromJson(orderJson);
        binding.setOrder(order);

        CartRvAdapter adapter = new CartRvAdapter(order.getCart());
        binding.cartRv.setAdapter(adapter);
        binding.cartRv.setLayoutManager(new LinearLayoutManager(this));
        binding.cartRv.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        binding.cartRv.setHasFixedSize(true);

        int orderStatusTitleRes = order.getOrderStatus().getTitleRes();
        binding.orderStatusTv.setText(orderStatusTitleRes);
        binding.contactUsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEmailIntent(order.getId());
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        final StoreInfo storeInfo = viewModel.storeInfoLiveData.getValue();
        if (storeInfo != null) {
            final Coordinate coordinate = storeInfo.getCoordinate();
            LatLng latLng = new LatLng(coordinate.getLatitude(), coordinate.getLongitude());
            googleMap.addMarker(new MarkerOptions().position(latLng));
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
            googleMap.getUiSettings().setAllGesturesEnabled(false);
            googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    Coordinate coordinate = storeInfo.getCoordinate();
                    Uri gmmIntentUri = Uri.parse("google.navigation:q=" + coordinate.getLatitude() + "," + coordinate.getLongitude());
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    mapIntent.setPackage("com.google.android.apps.maps");
                    if (mapIntent.resolveActivity(getPackageManager()) != null) {
                        startActivity(mapIntent);
                    }
                }
            });
        }
    }

    private void updateStoreInfo(@Nullable final StoreInfo storeInfo) {
        if (storeInfo != null) {
            binding.setStoreInfo(storeInfo);
            binding.directionsIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Coordinate coordinate = storeInfo.getCoordinate();
                    Uri gmmIntentUri = Uri.parse("geo:0,0?q=" +
                            coordinate.getLatitude() + "," +
                            coordinate.getLongitude() + "( " + storeInfo.getName() + ")");

                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    mapIntent.setPackage("com.google.android.apps.maps");
                    if (mapIntent.resolveActivity(getPackageManager()) != null) {
                        startActivity(mapIntent);
                    }
                }
            });
            MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.mapFragment);
            mapFragment.getMapAsync(this);
        }
    }

    private void sendEmailIntent(String orderId) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_EMAIL, new String[] {"support@pencake.com"});
        intent.putExtra(Intent.EXTRA_SUBJECT, "Order Support (" + orderId + ")");
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    public static Intent newIntent(FragmentActivity activity, String orderJson) {
        Intent intent = new Intent(activity, OrderDetailActivity.class);
        intent.putExtra(EXTRA_ORDER_JSON, orderJson);
        return intent;
    }
}