package com.timotiusoktorio.pencake.ui.orderdetail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapFragment
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.timotiusoktorio.pencake.R
import com.timotiusoktorio.pencake.data.model.Order
import com.timotiusoktorio.pencake.data.model.StoreInfo
import com.timotiusoktorio.pencake.data.source.DataManager
import com.timotiusoktorio.pencake.extensions.consume
import com.timotiusoktorio.pencake.extensions.observe
import com.timotiusoktorio.pencake.extensions.toLongDateFormat
import com.timotiusoktorio.pencake.extensions.withViewModel
import com.timotiusoktorio.pencake.ui.BaseActivity
import com.timotiusoktorio.pencake.ui.adapters.CartRvAdapter
import kotlinx.android.synthetic.main.activity_order_detail.*
import java.util.*
import javax.inject.Inject

class OrderDetailActivity : BaseActivity(), OnMapReadyCallback {

    @Inject lateinit var dataManager: DataManager

    private lateinit var viewModel: OrderDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_detail)
        component.inject(this)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val order = Order.fromJson(intent.getStringExtra(EXTRA_ORDER_JSON))
                ?: throw IllegalStateException("Order JSON was not sent here as intent extra")

        initViews(order)
        initViewModel()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean = when (item!!.itemId) {
        android.R.id.home -> consume { finish() }
        else -> super.onOptionsItemSelected(item)
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

    private fun initViews(order: Order) {
        val rvAdapter = CartRvAdapter(order.cart.toMutableList()) {}
        cartRv.adapter = rvAdapter
        cartRv.layoutManager = LinearLayoutManager(this)
        cartRv.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        cartRv.setHasFixedSize(true)

        orderDateTv.text = getString(R.string.string_format_order_date, Date(order.orderDate).toLongDateFormat())
        subtotalTv.text = getString(R.string.string_format_price, order.subtotal)
        nameTv.text = order.contactInfo.name
        emailTv.text = order.contactInfo.email
        phoneTv.text = order.contactInfo.phone

        orderStatusTv.setText(order.orderStatus.titleRes)
        contactUsButton.setOnClickListener { sendEmailIntent(order.id) }
    }

    private fun sendEmailIntent(orderId: String) {
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.data = Uri.parse("mailto:")
        intent.putExtra(Intent.EXTRA_EMAIL, arrayOf("support@pencake.com"))
        intent.putExtra(Intent.EXTRA_SUBJECT, "Order Support ($orderId)")
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        }
    }

    private fun initViewModel() {
        viewModel = withViewModel({ OrderDetailViewModel(dataManager) }) {
            observe(storeInfoLiveData, ::updateStoreInfo)
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

    companion object {

        const val EXTRA_ORDER_JSON = "EXTRA_ORDER_JSON"
    }
}