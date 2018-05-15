package com.timotiusoktorio.pencake.ui.menu

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.timotiusoktorio.pencake.R
import com.timotiusoktorio.pencake.data.model.KState
import com.timotiusoktorio.pencake.data.model.Product
import com.timotiusoktorio.pencake.data.source.DataManager
import com.timotiusoktorio.pencake.extensions.observe
import com.timotiusoktorio.pencake.extensions.withViewModel
import com.timotiusoktorio.pencake.ui.BaseActivity
import com.timotiusoktorio.pencake.ui.adapters.ProductsRvAdapter
import com.timotiusoktorio.pencake.ui.productdetail.ProductDetailActivity
import kotlinx.android.synthetic.main.fragment_menu_list.*
import org.jetbrains.anko.support.v4.startActivity
import javax.inject.Inject

class MenuListFragment : Fragment() {

    @Inject lateinit var dataManager: DataManager

    private lateinit var rvAdapter: ProductsRvAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_menu_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvAdapter = ProductsRvAdapter {
            startActivity<ProductDetailActivity>(ProductDetailActivity.EXTRA_PRODUCT_JSON to it.toJson())
        }
        productsRv.adapter = rvAdapter
        productsRv.layoutManager = GridLayoutManager(activity, 2)
        productsRv.setHasFixedSize(true)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as BaseActivity).component.inject(this)

        val categoryId = arguments?.getString(ARG_CATEGORY_ID)
                ?: throw IllegalStateException("Category ID was not sent here as an argument")

        withViewModel({ MenuListFragmentViewModel(dataManager, categoryId) }) {
            observe(productsLiveData, ::updateData)
            observe(stateLiveData, ::updateState)
        }
    }

    private fun updateData(data: List<Product>?) {
        data?.let { rvAdapter.updateData(it) }
    }

    private fun updateState(state: KState?) {
        state?.let {
            when (it) {
                KState.LOADING -> showLoading()
                KState.SUCCESS -> showData()
                KState.ERROR -> showError()
            }
        }
    }

    private fun showLoading() {
        productsRv.visibility = View.INVISIBLE
        progressBar.visibility = View.VISIBLE
        errorTv.visibility = View.GONE
    }

    private fun showData() {
        productsRv.visibility = View.VISIBLE
        progressBar.visibility = View.GONE
        errorTv.visibility = View.GONE
    }

    private fun showError() {
        productsRv.visibility = View.INVISIBLE
        progressBar.visibility = View.GONE
        errorTv.visibility = View.VISIBLE
    }

    companion object {

        const val ARG_CATEGORY_ID = "ARG_CATEGORY_ID"

        fun newInstance(categoryId: String): MenuListFragment {
            val args = Bundle()
            args.putString(ARG_CATEGORY_ID, categoryId)
            val fragment = MenuListFragment()
            fragment.arguments = args
            return fragment
        }
    }
}