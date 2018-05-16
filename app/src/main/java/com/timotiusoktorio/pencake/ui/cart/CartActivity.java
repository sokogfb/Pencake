package com.timotiusoktorio.pencake.ui.cart;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.timotiusoktorio.pencake.R;
import com.timotiusoktorio.pencake.data.model.CartItem;
import com.timotiusoktorio.pencake.data.model.ContactInfo;
import com.timotiusoktorio.pencake.data.model.Coordinate;
import com.timotiusoktorio.pencake.data.model.State;
import com.timotiusoktorio.pencake.data.model.StoreInfo;
import com.timotiusoktorio.pencake.databinding.ActivityCartBinding;
import com.timotiusoktorio.pencake.ui.BaseActivity;
import com.timotiusoktorio.pencake.ui.adapters.CartRvAdapter;
import com.timotiusoktorio.pencake.ui.dialogs.AlertDialogFragment;
import com.timotiusoktorio.pencake.ui.dialogs.ConfirmationDialogFragment;
import com.timotiusoktorio.pencake.ui.dialogs.DatePickerFragment;
import com.timotiusoktorio.pencake.ui.dialogs.TimePickerFragment;
import com.timotiusoktorio.pencake.ui.orderconfirmation.OrderConfirmationActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;

public class CartActivity extends BaseActivity implements
        DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener,
        OnMapReadyCallback, ConfirmationDialogFragment.OnConfirmationDialogPositive {

    @Inject CartViewModelFactory vmFactory;

    private ActivityCartBinding binding;
    private CartRvAdapter adapter;
    private CartViewModel viewModel;
    private MenuItem clearCartMenuItem;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_cart);
        getComponent().inject(this);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        setOnEditorActionListenerToTextFields(binding.nameTextField);
        setOnEditorActionListenerToTextFields(binding.emailTextField);
        setOnEditorActionListenerToTextFields(binding.phoneTextField);

        binding.datePickerTextField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerFragment datePickerFragment = new DatePickerFragment();
                datePickerFragment.show(getFragmentManager(), "datePicker");
            }
        });
        binding.timePickerTextField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerFragment timePickerFragment = new TimePickerFragment();
                timePickerFragment.show(getFragmentManager(), "timePicker");
            }
        });

        adapter = new CartRvAdapter(new ArrayList<CartItem>());
        adapter.setListener(new CartRvAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(CartItem cartItem) {
//                startActivity(AddToCartActivity.newIntent(CartActivity.this, cartItem.getProductJson(), cartItem.toJson()));
            }
        });
        binding.cartRv.setAdapter(adapter);
        binding.cartRv.setLayoutManager(new LinearLayoutManager(this));
        binding.cartRv.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        binding.cartRv.setHasFixedSize(true);

        binding.placeOrderButton.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPlaceOrderButtonClick();
            }
        });

        viewModel = ViewModelProviders.of(this, vmFactory).get(CartViewModel.class);
        viewModel.stateLiveData.observe(this, new Observer<State>() {
            @Override
            public void onChanged(@Nullable State state) {
                updateState(state);
            }
        });
        viewModel.cartLiveData.observe(this, new Observer<List<CartItem>>() {
            @Override
            public void onChanged(@Nullable List<CartItem> cart) {
                updateCart(cart);
            }
        });
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
        viewModel.contactInfoLiveData.observe(this, new Observer<ContactInfo>() {
            @Override
            public void onChanged(@Nullable ContactInfo contactInfo) {
                updateContactInfo(contactInfo);
            }
        });
        viewModel.pickupScheduleLiveData.observe(this, new Observer<Calendar>() {
            @Override
            public void onChanged(@Nullable Calendar pickupSchedule) {
                updatePickupSchedule(pickupSchedule);
            }
        });
        viewModel.storeInfoLiveData.observe(this, new Observer<StoreInfo>() {
            @Override
            public void onChanged(@Nullable StoreInfo storeInfo) {
                updateStoreInfo(storeInfo);
            }
        });
        viewModel.exitFlagLiveData.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean exitFlag) {
                exit(exitFlag);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        viewModel.loadCart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (isFinishing()) {
            String name = binding.nameTextField.getText().toString();
            String email = binding.emailTextField.getText().toString();
            String phone = binding.phoneTextField.getText().toString();
            viewModel.saveContactInfo(name, email, phone);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_cart, menu);
        clearCartMenuItem = menu.findItem(R.id.action_clear_cart);
        State state = viewModel.stateLiveData.getValue();
        if (state != null) {
            clearCartMenuItem.setVisible(state == State.SUCCESS);
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
            case R.id.action_clear_cart: {
                showConfirmationDialog();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        viewModel.onDateSet(year, month, dayOfMonth);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        viewModel.onTimeSet(hourOfDay, minute);
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
        }
    }

    @Override
    public void onPositiveButtonClick() {
        viewModel.clearCart();
    }

    private void setOnEditorActionListenerToTextFields(EditText editText) {
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                        v.clearFocus();
                        binding.mainContainer.requestFocus(); // direct the focus to the parent view.
                        return true;
                    }
                }
                return false;
            }
        });
    }

    private void onPlaceOrderButtonClick() {
        String name = binding.nameTextField.getText().toString();
        String email = binding.emailTextField.getText().toString();
        String phone = binding.phoneTextField.getText().toString();
        String date = binding.datePickerTextField.getText().toString();
        String time = binding.timePickerTextField.getText().toString();
        if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || date.isEmpty() || time.isEmpty()) {
            showErrorDialog();
        } else {
            ContactInfo contactInfo = new ContactInfo(name, email, phone);
            viewModel.placeOrder(contactInfo);
        }
    }

    private void updateState(@Nullable State state) {
        if (state != null) {
            binding.setState(state);
            if (clearCartMenuItem != null) {
                clearCartMenuItem.setVisible(state == State.SUCCESS);
            }
        }
    }

    private void updateCart(@Nullable List<CartItem> cart) {
        if (cart != null) {
            adapter.updateData(cart);
        }
    }

    private void updateQuantity(@Nullable Integer quantity) {
        if (quantity != null) {
            binding.setCartQuantity(quantity);
        }
    }

    private void updateSubtotal(@Nullable Integer subtotal) {
        if (subtotal != null) {
            binding.setCartSubtotal(subtotal);
        }
    }

    private void updateContactInfo(@Nullable ContactInfo contactInfo) {
        if (contactInfo != null) {
            binding.setContactInfo(contactInfo);
        }
    }

    private void updatePickupSchedule(@Nullable Calendar pickupSchedule) {
        if (pickupSchedule != null) {
            binding.setPickupSchedule(pickupSchedule);
        }
    }

    private void updateStoreInfo(@Nullable final StoreInfo storeInfo) {
        if (storeInfo != null) {
            binding.setStoreInfo(storeInfo);
            binding.directionsIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Coordinate coordinate = storeInfo.getCoordinate();
                    Uri gmmIntentUri = Uri.parse("google.navigation:q=" + coordinate.getLatitude() + "," + coordinate.getLongitude());
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

    private void exit(@Nullable Boolean exitFlag) {
        if (exitFlag != null && exitFlag) {
            startActivity(new Intent(this, OrderConfirmationActivity.class));
        }
    }

    private void showConfirmationDialog() {
        ConfirmationDialogFragment dialogFragment = ConfirmationDialogFragment.newInstance(R.string.dialog_confirmation_clear_cart, R.string.label_remove);
        dialogFragment.show(getSupportFragmentManager(), "clearCartConfirmationDialog");
    }

    private void showErrorDialog() {
        String dialogTitle = getString(R.string.dialog_title_place_order_error);
        String dialogMessage = getString(R.string.dialog_message_place_order_error);
        AlertDialogFragment dialogFragment = AlertDialogFragment.newInstance(dialogTitle, dialogMessage);
        dialogFragment.show(getFragmentManager(), "placeOrderErrorDialog");
    }
}