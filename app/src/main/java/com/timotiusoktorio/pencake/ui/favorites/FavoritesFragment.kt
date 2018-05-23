package com.timotiusoktorio.pencake.ui.favorites

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.timotiusoktorio.pencake.R
import com.timotiusoktorio.pencake.data.model.State
import com.timotiusoktorio.pencake.data.model.Product
import com.timotiusoktorio.pencake.data.source.DataManager
import com.timotiusoktorio.pencake.extensions.observe
import com.timotiusoktorio.pencake.extensions.withViewModel
import com.timotiusoktorio.pencake.ui.BaseActivity
import com.timotiusoktorio.pencake.ui.adapters.ProductsRvAdapter
import com.timotiusoktorio.pencake.ui.productdetail.ProductDetailActivity
import kotlinx.android.synthetic.main.fragment_favorites.*
import org.jetbrains.anko.support.v4.startActivity
import javax.inject.Inject

class FavoritesFragment : Fragment() {

    @Inject lateinit var dataManager: DataManager

    private lateinit var rvAdapter: ProductsRvAdapter
    private lateinit var viewModel: FavoritesViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_favorites, container, false)
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
        requireActivity().setTitle(R.string.label_favorites)

        viewModel = withViewModel({ FavoritesViewModel(dataManager) }) {
            observe(stateLiveData, ::updateState)
            observe(productsLiveData, ::updateData)
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.refreshFavorites()
    }

    private fun updateState(state: State?) {
        state?.let {
            when (it) {
                State.LOADING -> {
                    productsRv.visibility = View.INVISIBLE
                    progressBar.visibility = View.VISIBLE
                    errorView.visibility = View.GONE
                }
                State.SUCCESS -> {
                    productsRv.visibility = View.VISIBLE
                    progressBar.visibility = View.GONE
                    errorView.visibility = View.GONE
                }
                State.ERROR -> {
                    productsRv.visibility = View.GONE
                    progressBar.visibility = View.GONE
                    errorView.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun updateData(data: List<Product>?) {
        data?.let { rvAdapter.updateData(it) }
    }

    companion object {

        fun newInstance(): FavoritesFragment {
            val args = Bundle()
            val fragment = FavoritesFragment()
            fragment.arguments = args
            return fragment
        }
    }
}