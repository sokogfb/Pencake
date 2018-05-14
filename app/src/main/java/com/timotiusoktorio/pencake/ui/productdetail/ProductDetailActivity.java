package com.timotiusoktorio.pencake.ui.productdetail;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.squareup.picasso.Picasso;
import com.timotiusoktorio.pencake.R;
import com.timotiusoktorio.pencake.data.model.Product;
import com.timotiusoktorio.pencake.databinding.ActivityProductDetailBinding;
import com.timotiusoktorio.pencake.ui.BaseActivity;
import com.timotiusoktorio.pencake.ui.addtocart.AddToCartActivity;
import com.timotiusoktorio.pencake.ui.cart.CartActivity;

import javax.inject.Inject;

public class ProductDetailActivity extends BaseActivity {

    public static final String EXTRA_PRODUCT_JSON = "EXTRA_PRODUCT_JSON";

    @Inject
    ProductDetailViewModelFactory vmFactory;

    private ActivityProductDetailBinding binding;
    private ProductDetailViewModel viewModel;
    private MenuItem favoriteMenuItem;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_product_detail);
        getComponent().inject(this);

        setSupportActionBar(binding.toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }

        String productJson = getIntent().getStringExtra(EXTRA_PRODUCT_JSON);
        if (productJson == null) {
            throw new IllegalStateException("Product JSON was not sent here as an intent extra");
        }

        final Product product = Product.fromJson(productJson);
        binding.setProduct(product);
        binding.setCartQuantity(0);
        binding.setCartSubtotal(0);
        binding.addToCartFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(AddToCartActivity.newIntent(ProductDetailActivity.this, product.toJson(), null));
            }
        });
        binding.viewCartButton.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProductDetailActivity.this, CartActivity.class));
            }
        });

        Picasso.get().load(product.getImageUrls().get(0)).centerCrop().fit()
                .placeholder(R.drawable.placeholder_image)
                .into(binding.productPhotoIv);

        viewModel = ViewModelProviders.of(this, vmFactory).get(ProductDetailViewModel.class);
        viewModel.setProduct(product);
        viewModel.checkFavorite();
        viewModel.cartQuantityLiveData.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer cartQuantity) {
                updateCartQuantity(cartQuantity);
            }
        });
        viewModel.cartSubtotalLiveData.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer cartSubtotal) {
                updateCartSubtotal(cartSubtotal);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        viewModel.loadCart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_product_detail, menu);
        favoriteMenuItem = menu.findItem(R.id.action_favorite);
        viewModel.favoriteFlagLiveData.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean favoriteFlag) {
                updateFavoriteMenuItem(favoriteFlag);
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_favorite: {
                viewModel.updateFavorite();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateCartQuantity(@Nullable Integer cartQuantity) {
        if (cartQuantity != null) {
            binding.setCartQuantity(cartQuantity);
        }
    }

    private void updateCartSubtotal(@Nullable Integer cartSubtotal) {
        if (cartSubtotal != null) {
            binding.setCartSubtotal(cartSubtotal);
        }
    }

    private void updateFavoriteMenuItem(@Nullable Boolean favoriteFlag) {
        if (favoriteFlag != null) {
            favoriteMenuItem.setIcon(favoriteFlag ? R.drawable.ic_favorite : R.drawable.ic_favorite_border);
        }
    }

    public static Intent newIntent(FragmentActivity activity, String productJson) {
        Intent intent = new Intent(activity, ProductDetailActivity.class);
        intent.putExtra(EXTRA_PRODUCT_JSON, productJson);
        return intent;
    }
}