package com.timotiusoktorio.pencake.ui.addtocart;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.timotiusoktorio.pencake.R;
import com.timotiusoktorio.pencake.data.model.CartItem;
import com.timotiusoktorio.pencake.data.model.Product;
import com.timotiusoktorio.pencake.databinding.ActivityAddToCartBinding;
import com.timotiusoktorio.pencake.ui.BaseActivity;
import com.timotiusoktorio.pencake.ui.dialogs.ConfirmationDialogFragment;

import java.util.List;

import javax.inject.Inject;

public class AddToCartActivity extends BaseActivity implements ConfirmationDialogFragment.OnConfirmationDialogPositive {

    private static final String EXTRA_PRODUCT_JSON = "EXTRA_PRODUCT_JSON";
    private static final String EXTRA_CART_ITEM_JSON = "EXTRA_CART_ITEM_JSON";

    @Inject AddToCartViewModelFactory vmFactory;

    private ActivityAddToCartBinding binding;
    private AddToCartViewModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_to_cart);
        getComponent().inject(this);

        String productJson = getIntent().getStringExtra(EXTRA_PRODUCT_JSON);
        if (productJson == null) {
            throw new IllegalStateException("Product JSON was not sent here as an intent extra");
        }

        viewModel = ViewModelProviders.of(this, vmFactory).get(AddToCartViewModel.class);
        viewModel.quantityLiveData.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer quantity) {
                updateQuantity(quantity);
            }
        });
        viewModel.subtotalLiveData.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer subtotal) {
                updateSubtotal(subtotal);
            }
        });
        viewModel.exitFlagLiveData.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean exitFlag) {
                exit(exitFlag);
            }
        });

        Product product = Product.fromJson(productJson);
        CartItem cartItem = CartItem.fromJson(getIntent().getStringExtra(EXTRA_CART_ITEM_JSON));
        viewModel.setInitialSizeAndQuantity(product, cartItem);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(product.getName());
            actionBar.setHomeAsUpIndicator(R.drawable.ic_close);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        List<String> sizes = product.getSizes();
        int lastSelectedPosition = (cartItem != null) ? cartItem.getSelectedSizeIndex() : 0;
        SizeRvAdapter adapter = new SizeRvAdapter(sizes, lastSelectedPosition, new SizeRvAdapter.OnSizeSelectedListener() {
            @Override
            public void onSizeSelected(int sizePosition) {
                viewModel.setSelectedSizeIndex(sizePosition);
                viewModel.setQuantity(1);
            }
        });
        binding.sizeRv.setAdapter(adapter);
        binding.sizeRv.setLayoutManager(new LinearLayoutManager(this));
        binding.sizeRv.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        binding.sizeRv.setHasFixedSize(true);

        if (cartItem != null) {
            binding.specialRequestsTextField.setText(cartItem.getSpecialRequests());
        }

        binding.minusQtyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.onMinusButtonClick();
            }
        });
        binding.plusQtyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.onPlusButtonClick();
            }
        });
        binding.saveCartButton.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.onSaveCartClick(binding.specialRequestsTextField.getText().toString());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_to_cart, menu);
        MenuItem removeFromCartItem = menu.findItem(R.id.action_remove_from_cart);
        if (removeFromCartItem != null && !viewModel.isUpdatingCart()) {
            removeFromCartItem.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                finish();
                return true;
            }
            case R.id.action_remove_from_cart: {
                showConfirmationDialog();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateQuantity(@Nullable Integer quantity) {
        if (quantity != null) {
            binding.setQuantity(quantity);
        }
    }

    private void updateSubtotal(@Nullable Integer subtotal) {
        if (subtotal != null) {
            binding.setSubtotal(subtotal);
        }
    }

    private void exit(@Nullable Boolean exitFlag) {
        if (exitFlag != null && exitFlag) {
            finish();
        }
    }

    private void showConfirmationDialog() {
        ConfirmationDialogFragment dialogFragment = ConfirmationDialogFragment.newInstance(R.string.dialog_confirmation_remove_from_cart, R.string.label_remove);
        dialogFragment.show(getSupportFragmentManager(), "removeFromCartConfirmationDialog");
    }

    @Override
    public void onPositiveButtonClick() {
        viewModel.removeFromCart();
    }

    public static Intent newIntent(FragmentActivity activity, String productJson, @Nullable String cartItemJson) {
        Intent intent = new Intent(activity, AddToCartActivity.class);
        intent.putExtra(EXTRA_PRODUCT_JSON, productJson);
        intent.putExtra(EXTRA_CART_ITEM_JSON, cartItemJson);
        return intent;
    }
}