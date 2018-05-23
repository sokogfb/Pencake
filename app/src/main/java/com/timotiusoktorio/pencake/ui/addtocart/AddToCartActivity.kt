package com.timotiusoktorio.pencake.ui.addtocart

import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import com.timotiusoktorio.pencake.R
import com.timotiusoktorio.pencake.data.model.CartItem
import com.timotiusoktorio.pencake.data.model.Product
import com.timotiusoktorio.pencake.data.source.DataManager
import com.timotiusoktorio.pencake.extensions.afterTextChanged
import com.timotiusoktorio.pencake.extensions.consume
import com.timotiusoktorio.pencake.extensions.observe
import com.timotiusoktorio.pencake.extensions.withViewModel
import com.timotiusoktorio.pencake.ui.BaseActivity
import com.timotiusoktorio.pencake.ui.adapters.SizeRvAdapter
import com.timotiusoktorio.pencake.ui.dialogs.ConfirmationDialogFragment
import kotlinx.android.synthetic.main.activity_add_to_cart.*
import javax.inject.Inject

class AddToCartActivity : BaseActivity(), ConfirmationDialogFragment.OnConfirmationDialogPositive {

    @Inject lateinit var dataManager: DataManager

    private lateinit var viewModel: AddToCartViewModel
    private var isUpdating = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_to_cart)
        component.inject(this)

        val bundle = intent.getBundleExtra(EXTRA_BUNDLE)
        val product = Product.fromJson(bundle.getString(KEY_PRODUCT_JSON))
                ?: throw IllegalStateException("Product JSON was not sent here as intent extra")

        val cartItem = CartItem.fromJson(bundle.getString(KEY_CART_ITEM_JSON))
        isUpdating = (cartItem != null)

        initActionBar(product.name)
        initViews(product, cartItem)
        initViewModel(product, cartItem)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean = consume {
        menuInflater.inflate(R.menu.menu_add_to_cart, menu)
        menu!!.findItem(R.id.action_remove_from_cart).isVisible = isUpdating
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean = when (item!!.itemId) {
        android.R.id.home -> consume { finish() }
        R.id.action_remove_from_cart -> consume { showConfirmationDialog() }
        else -> super.onOptionsItemSelected(item)
    }

    override fun onPositiveButtonClick() {
        viewModel.removeFromCart()
    }

    private fun initActionBar(actionBarTitle: String) {
        supportActionBar?.apply {
            title = actionBarTitle
            setHomeAsUpIndicator(R.drawable.ic_close)
            setDisplayHomeAsUpEnabled(true)
        }
    }

    private fun initViews(product: Product, cartItem: CartItem?) {
        val lastSelectedPosition = cartItem?.selectedSizeIndex ?: 0
        val rvAdapter = SizeRvAdapter(product.sizes, lastSelectedPosition) {
            viewModel.setSelectedSizeIndex(it)
            viewModel.setQuantity(1)
        }
        sizeRv.adapter = rvAdapter
        sizeRv.layoutManager = LinearLayoutManager(this)
        sizeRv.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        sizeRv.setHasFixedSize(true)

        specialRequestsTextField.setText(cartItem?.specialRequests)
        specialRequestsTextField.afterTextChanged { viewModel.setSpecialRequests(it.toString()) }

        minusQtyBtn.setOnClickListener { viewModel.decreaseQuantity() }
        plusQtyBtn.setOnClickListener { viewModel.increaseQuantity() }
        saveCartButton.setOnClickListener { viewModel.addToCart() }
    }

    private fun initViewModel(product: Product, cartItem: CartItem?) {
        viewModel = withViewModel({ AddToCartViewModel(dataManager, product, cartItem) }) {
            observe(quantityLiveData, ::updateQuantity)
            observe(subtotalLiveData, ::updateSubtotal)
            observe(exitFlagLiveData, ::exit)
        }
    }

    private fun updateQuantity(quantity: Int?) {
        quantity?.let {
            quantityTv.text = it.toString()
            cartQuantityTv.text = it.toString()
        }
    }

    private fun updateSubtotal(subtotal: Int?) {
        subtotal?.let { cartSubtotalTv.text = getString(R.string.string_format_price, it) }
    }

    private fun exit(exitFlag: Boolean?) {
        exitFlag?.let { if (it) finish() }
    }

    private fun showConfirmationDialog() {
        val dialogFragment = ConfirmationDialogFragment.newInstance(R.string.dialog_confirmation_remove_from_cart, R.string.label_remove)
        dialogFragment.show(supportFragmentManager, "removeFromCartConfirmationDialog")
    }

    companion object {

        const val EXTRA_BUNDLE = "EXTRA_BUNDLE"
        const val KEY_PRODUCT_JSON = "KEY_PRODUCT_JSON"
        const val KEY_CART_ITEM_JSON = "KEY_CART_ITEM_JSON"
    }
}