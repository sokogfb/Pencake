package com.timotiusoktorio.pencake.ui.cart

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.DatePicker
import android.widget.TimePicker
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapFragment
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.timotiusoktorio.pencake.R
import com.timotiusoktorio.pencake.data.model.CartItem
import com.timotiusoktorio.pencake.data.model.ContactInfo
import com.timotiusoktorio.pencake.data.model.KState
import com.timotiusoktorio.pencake.data.model.StoreInfo
import com.timotiusoktorio.pencake.data.source.DataManager
import com.timotiusoktorio.pencake.extensions.*
import com.timotiusoktorio.pencake.ui.BaseActivity
import com.timotiusoktorio.pencake.ui.adapters.CartRvAdapter
import com.timotiusoktorio.pencake.ui.addtocart.AddToCartActivity
import com.timotiusoktorio.pencake.ui.dialogs.AlertDialogFragment
import com.timotiusoktorio.pencake.ui.dialogs.ConfirmationDialogFragment
import com.timotiusoktorio.pencake.ui.dialogs.DatePickerFragment
import com.timotiusoktorio.pencake.ui.dialogs.TimePickerFragment
import com.timotiusoktorio.pencake.ui.orderconfirmation.OrderConfirmationActivity
import kotlinx.android.synthetic.main.activity_cart.*
import org.jetbrains.anko.bundleOf
import org.jetbrains.anko.startActivity
import java.util.*
import javax.inject.Inject

class CartActivity : BaseActivity(), DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener,
        ConfirmationDialogFragment.OnConfirmationDialogPositive, OnMapReadyCallback {

    @Inject lateinit var dataManager: DataManager

    private lateinit var rvAdapter: CartRvAdapter
    private lateinit var viewModel: CartViewModel
    private var clearCartMenuItem: MenuItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)
        component.inject(this)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        initViews()
        initViewModel()
    }

    override fun onStart() {
        super.onStart()
        viewModel.loadCart()
    }

    override fun onStop() {
        super.onStop()
        if (isFinishing) {
            viewModel.saveContactInfo(nameTextField.text.toString(),
                    emailTextField.text.toString(), phoneTextField.text.toString())
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean = consume {
        menuInflater.inflate(R.menu.menu_cart, menu)
        clearCartMenuItem = menu!!.findItem(R.id.action_clear_cart)
        clearCartMenuItem?.isVisible = false
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean = when (item!!.itemId) {
        android.R.id.home -> consume { finish() }
        R.id.action_clear_cart -> consume { showConfirmationDialog() }
        else -> super.onOptionsItemSelected(item)
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        viewModel.onDateSet(year, month, dayOfMonth)
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        viewModel.onTimeSet(hourOfDay, minute)
    }

    override fun onPositiveButtonClick() {
        viewModel.clearCart()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        viewModel.storeInfoLiveData.value?.let { storeInfo ->
            val coordinate = storeInfo.coordinate
            val latLng = LatLng(coordinate.latitude, coordinate.longitude)
            googleMap.addMarker(MarkerOptions().position(latLng))
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16F))
            googleMap.uiSettings.setAllGesturesEnabled(false)
            googleMap.setOnMapClickListener {
                val uri = Uri.parse("geo:0,0?q=${coordinate.latitude},${coordinate.longitude}(${storeInfo.name})")
                val mapIntent = Intent(Intent.ACTION_VIEW, uri)
                mapIntent.`package` = "com.google.android.apps.maps"
                if (mapIntent.resolveActivity(packageManager) != null) {
                    startActivity(mapIntent)
                }
            }
        }
    }

    private fun initViews() {
        rvAdapter = CartRvAdapter(mutableListOf()) {
            startActivity<AddToCartActivity>(AddToCartActivity.EXTRA_BUNDLE to bundleOf(
                    AddToCartActivity.KEY_PRODUCT_JSON to it.productJson,
                    AddToCartActivity.KEY_CART_ITEM_JSON to it.toJson()))
        }
        cartRv.adapter = rvAdapter
        cartRv.layoutManager = LinearLayoutManager(this)
        cartRv.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        cartRv.setHasFixedSize(true)

        nameTextField.onDoneImeAction { mainContainer.requestFocus() }
        emailTextField.onDoneImeAction { mainContainer.requestFocus() }
        phoneTextField.onDoneImeAction { mainContainer.requestFocus() }

        datePickerTextField.setOnClickListener { DatePickerFragment().show(fragmentManager, "datePicker") }
        timePickerTextField.setOnClickListener { TimePickerFragment().show(fragmentManager, "timePicker") }

        placeOrderButton.setOnClickListener {
            viewModel.placeOrder(nameTextField.text.toString(),
                    emailTextField.text.toString(), phoneTextField.text.toString(),
                    datePickerTextField.text.toString(), timePickerTextField.text.toString())
        }
    }

    private fun initViewModel() {
        viewModel = withViewModel({ CartViewModel(dataManager) }) {
            observe(stateLiveData, ::updateState)
            observe(cartLiveData, ::updateCart)
            observe(quantityLiveData, ::updateQuantity)
            observe(subtotalLiveData, ::updateSubtotal)
            observe(contactInfoLiveData, ::updateContactInfo)
            observe(pickupScheduleLiveData, ::updatePickupSchedule)
            observe(storeInfoLiveData, ::updateStoreInfo)
            observe(exitFlagLiveData, ::exit)
            observe(placeOrderErrorFlagLiveData, ::placeOrderError)
        }
    }

    private fun updateState(state: KState?) {
        state?.let {
            when (it) {
                KState.LOADING -> {
                    nestedScrollView.visibility = View.INVISIBLE
                    placeOrderButton.visibility = View.GONE
                    progressBar.visibility = View.VISIBLE
                    errorTv.visibility = View.GONE
                    clearCartMenuItem?.isVisible = false
                }
                KState.SUCCESS -> {
                    nestedScrollView.visibility = View.VISIBLE
                    placeOrderButton.visibility = View.VISIBLE
                    progressBar.visibility = View.GONE
                    errorTv.visibility = View.GONE
                    clearCartMenuItem?.isVisible = true
                }
                KState.ERROR -> {
                    nestedScrollView.visibility = View.GONE
                    placeOrderButton.visibility = View.GONE
                    progressBar.visibility = View.GONE
                    errorTv.visibility = View.VISIBLE
                    clearCartMenuItem?.isVisible = false
                }
            }
        }
    }

    private fun updateCart(cart: List<CartItem>?) {
        cart?.let { rvAdapter.updateData(it) }
    }

    private fun updateQuantity(quantity: Int?) {
        quantity?.let { cartQuantityTv.text = it.toString() }
    }

    private fun updateSubtotal(subtotal: Int?) {
        subtotal?.let {
            val subtotalText = getString(R.string.string_format_price, it)
            subtotalTv.text = subtotalText
            cartSubtotalTv.text = subtotalText
        }
    }

    private fun updateContactInfo(contactInfo: ContactInfo?) {
        contactInfo?.let {
            nameTextField.setText(it.name)
            emailTextField.setText(it.email)
            phoneTextField.setText(it.phone)
        }
    }

    private fun updatePickupSchedule(pickupSchedule: Calendar?) {
        pickupSchedule?.let {
            datePickerTextField.setText(it.time.toLongDateFormat())
            timePickerTextField.setText(it.time.toTimeFormat())
        }
    }

    private fun updateStoreInfo(storeInfo: StoreInfo?) {
        storeInfo?.let {
            storeNameTv.text = it.name
            storeAddressTv.text = it.address
            storePhoneNumberTv.text = it.phone
            directionsIcon.setOnClickListener { _ ->
                val coordinate = it.coordinate
                val uri = Uri.parse("google.navigation:q=${coordinate.latitude},${coordinate.longitude}")
                val mapIntent = Intent(Intent.ACTION_VIEW, uri)
                mapIntent.`package` = "com.google.android.apps.maps"
                if (mapIntent.resolveActivity(packageManager) != null) {
                    startActivity(mapIntent)
                }
            }
            val mapFragment = fragmentManager.findFragmentById(R.id.mapFragment) as MapFragment
            mapFragment.getMapAsync(this)
        }
    }

    private fun exit(exitFlag: Boolean?) {
        exitFlag?.let { if (it) startActivity<OrderConfirmationActivity>() }
    }

    private fun placeOrderError(placeOrderErrorFlag: Boolean?) {
        placeOrderErrorFlag?.let { if (it) showErrorDialog() }
    }

    private fun showErrorDialog() {
        val dialogTitle = getString(R.string.dialog_title_place_order_error)
        val dialogMessage = getString(R.string.dialog_message_place_order_error)
        val dialogFragment = AlertDialogFragment.newInstance(dialogTitle, dialogMessage)
        dialogFragment.show(fragmentManager, "placeOrderErrorDialog")
    }

    private fun showConfirmationDialog() {
        val dialogFragment = ConfirmationDialogFragment.newInstance(R.string.dialog_confirmation_clear_cart, R.string.label_remove)
        dialogFragment.show(supportFragmentManager, "clearCartConfirmationDialog")
    }
}