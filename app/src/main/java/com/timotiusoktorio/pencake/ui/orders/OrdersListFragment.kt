package com.timotiusoktorio.pencake.ui.orders

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.timotiusoktorio.pencake.R
import com.timotiusoktorio.pencake.data.model.State
import com.timotiusoktorio.pencake.data.model.Order
import com.timotiusoktorio.pencake.data.source.DataManager
import com.timotiusoktorio.pencake.extensions.observe
import com.timotiusoktorio.pencake.extensions.withViewModel
import com.timotiusoktorio.pencake.ui.BaseActivity
import com.timotiusoktorio.pencake.ui.adapters.OrdersRvAdapter
import com.timotiusoktorio.pencake.ui.orderdetail.OrderDetailActivity
import kotlinx.android.synthetic.main.fragment_orders_list.*
import org.jetbrains.anko.bundleOf
import org.jetbrains.anko.support.v4.startActivity
import javax.inject.Inject

class OrdersListFragment : Fragment() {

    @Inject lateinit var dataManager: DataManager

    private lateinit var rvAdapter: OrdersRvAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_orders_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvAdapter = OrdersRvAdapter {
            startActivity<OrderDetailActivity>(OrderDetailActivity.EXTRA_ORDER_JSON to it.toJson())
        }
        ordersRv.adapter = rvAdapter
        ordersRv.layoutManager = LinearLayoutManager(activity)
        ordersRv.addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
        ordersRv.setHasFixedSize(true)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as BaseActivity).component.inject(this)

        val tabIndex = arguments?.getInt(ARG_TAB_INDEX) ?: 0
        withViewModel({ OrdersListFragmentViewModel(dataManager, tabIndex) }) {
            observe(stateLiveData, ::updateState)
            observe(ordersLiveData, ::updateData)
        }
    }

    private fun updateState(state: State?) {
        state?.let {
            when (it) {
                State.LOADING -> {
                    ordersRv.visibility = View.INVISIBLE
                    progressBar.visibility = View.VISIBLE
                    errorTv.visibility = View.GONE
                }
                State.SUCCESS -> {
                    ordersRv.visibility = View.VISIBLE
                    progressBar.visibility = View.GONE
                    errorTv.visibility = View.GONE
                }
                State.ERROR -> {
                    ordersRv.visibility = View.GONE
                    progressBar.visibility = View.GONE
                    errorTv.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun updateData(data: List<Order>?) {
        data?.let { rvAdapter.updateData(it) }
    }

    companion object {

        const val ARG_TAB_INDEX = "ARG_TAB_INDEX"

        fun newInstance(tabIndex: Int): OrdersListFragment {
            val args = bundleOf(ARG_TAB_INDEX to tabIndex)
            val fragment = OrdersListFragment()
            fragment.arguments = args
            return fragment
        }
    }
}