package com.timotiusoktorio.pencake.ui.orders

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.timotiusoktorio.pencake.R
import kotlinx.android.synthetic.main.fragment_orders.*

class OrdersFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_orders, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val tabs = resources.getStringArray(R.array.orders_tab_titles).toList()
        val pagerAdapter = PagerAdapter(requireFragmentManager(), tabs)
        viewPager.adapter = pagerAdapter
        tabLayout.setupWithViewPager(viewPager)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        requireActivity().setTitle(R.string.label_orders)
    }

    private inner class PagerAdapter(fm: FragmentManager, private val tabs: List<String>) : FragmentStatePagerAdapter(fm) {

        override fun getItem(position: Int): Fragment = OrdersListFragment.newInstance(position)

        override fun getPageTitle(position: Int): CharSequence? = tabs[position]

        override fun getCount(): Int = tabs.size
    }

    companion object {

        fun newInstance(): OrdersFragment {
            val args = Bundle()
            val fragment = OrdersFragment()
            fragment.arguments = args
            return fragment
        }
    }
}