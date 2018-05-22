package com.timotiusoktorio.pencake.ui.productdetail

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.timotiusoktorio.pencake.R
import com.timotiusoktorio.pencake.data.model.Product
import com.timotiusoktorio.pencake.data.source.DataManager
import com.timotiusoktorio.pencake.extensions.consume
import com.timotiusoktorio.pencake.extensions.loadUrl
import com.timotiusoktorio.pencake.extensions.observe
import com.timotiusoktorio.pencake.extensions.withViewModel
import com.timotiusoktorio.pencake.ui.BaseActivity
import com.timotiusoktorio.pencake.ui.addtocart.AddToCartActivity
import com.timotiusoktorio.pencake.ui.cart.CartActivity
import kotlinx.android.synthetic.main.activity_product_detail.*
import org.jetbrains.anko.bundleOf
import org.jetbrains.anko.startActivity
import javax.inject.Inject

class ProductDetailActivity : BaseActivity() {

    @Inject lateinit var dataManager: DataManager

    private lateinit var viewModel: ProductDetailViewModel
    private var favoriteMenuItem: MenuItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)
        component.inject(this)

        val product = Product.fromJson(intent.getStringExtra(EXTRA_PRODUCT_JSON))
                ?: throw IllegalStateException("Product JSON was not sent here as intent extra")

        initActionBar()
        initViews(product)
        initViewModel(product)
    }

    override fun onStart() {
        super.onStart()
        viewModel.loadCart()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean = consume {
        menuInflater.inflate(R.menu.menu_product_detail, menu)
        favoriteMenuItem = menu?.findItem(R.id.action_favorite)
        viewModel.checkFavorite()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean = when (item!!.itemId) {
        R.id.action_favorite -> consume { viewModel.updateFavorite() }
        else -> super.onOptionsItemSelected(item)
    }

    private fun initActionBar() {
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowTitleEnabled(false)
        }
    }

    private fun initViews(product: Product) {
        productNameTv.text = product.name
        productDescTv.text = product.description
        productPriceTv.text = product.combinedPrices
        productTimeOfWorkTv.text = product.timeOfWorkDesc
        productPhotoIv.loadUrl(product.imageUrls[0])

        addToCartFab.setOnClickListener {
            startActivity<AddToCartActivity>(AddToCartActivity.EXTRA_BUNDLE to bundleOf(
                    AddToCartActivity.KEY_PRODUCT_JSON to product.toJson(),
                    AddToCartActivity.KEY_CART_ITEM_JSON to null))
        }
        viewCartButton.setOnClickListener { startActivity<CartActivity>() }
    }

    private fun initViewModel(product: Product) {
        viewModel = withViewModel({ ProductDetailViewModel(dataManager, product) }) {
            observe(cartQuantityLiveData, ::updateCartQuantity)
            observe(cartSubtotalLiveData, ::updateCartSubtotal)
            observe(favoriteFlagLiveData, ::updateFavoriteMenuItem)
        }
    }

    private fun updateCartQuantity(cartQuantity: Int?) {
        cartQuantity?.let {
            cartQuantityTv.text = it.toString()
            viewCartButton.visibility = if (it > 0) View.VISIBLE else View.GONE
        }
    }

    private fun updateCartSubtotal(cartSubtotal: Int?) {
        cartSubtotal?.let { cartSubtotalTv.text = getString(R.string.string_format_price, it) }
    }

    private fun updateFavoriteMenuItem(favoriteFlag: Boolean?) {
        favoriteFlag?.let { favoriteMenuItem?.setIcon(if (it) R.drawable.ic_favorite else R.drawable.ic_favorite_border) }
    }

    companion object {

        const val EXTRA_PRODUCT_JSON = "EXTRA_PRODUCT_JSON"
    }
}