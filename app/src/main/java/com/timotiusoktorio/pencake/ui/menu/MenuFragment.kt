package com.timotiusoktorio.pencake.ui.menu

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.timotiusoktorio.pencake.R
import com.timotiusoktorio.pencake.data.model.Category
import com.timotiusoktorio.pencake.data.model.State
import com.timotiusoktorio.pencake.data.source.DataManager
import com.timotiusoktorio.pencake.extensions.observe
import com.timotiusoktorio.pencake.extensions.withViewModel
import com.timotiusoktorio.pencake.ui.BaseActivity
import com.timotiusoktorio.pencake.ui.cart.CartActivity
import kotlinx.android.synthetic.main.fragment_menu.*
import org.jetbrains.anko.support.v4.startActivity
import javax.inject.Inject

class MenuFragment : Fragment() {

    @Inject lateinit var dataManager: DataManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cartFab.setOnClickListener { startActivity<CartActivity>() }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as BaseActivity).component.inject(this)
        requireActivity().setTitle(R.string.label_menu)

        withViewModel({ MenuFragmentViewModel(dataManager) }) {
            observe(stateLiveData, ::updateState)
            observe(categoriesLiveData, ::updateData)
        }
    }

    private fun updateState(state: State?) {
        state?.let {
            when (it) {
                State.LOADING -> {
                    progressBar.visibility = View.VISIBLE
                    errorTv.visibility = View.GONE
                }
                State.SUCCESS -> {
                    progressBar.visibility = View.GONE
                    errorTv.visibility = View.GONE
                }
                State.ERROR -> {
                    progressBar.visibility = View.GONE
                    errorTv.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun updateData(data: List<Category>?) {
        data?.let {
            viewPager.adapter = PagerAdapter(requireFragmentManager(), data)
            tabLayout.setupWithViewPager(viewPager)
        }
    }

    private inner class PagerAdapter(fm: FragmentManager, private val categories: List<Category>) : FragmentStatePagerAdapter(fm) {

        override fun getItem(position: Int): Fragment = MenuListFragment.newInstance(categories[position].id)

        override fun getPageTitle(position: Int): CharSequence? = categories[position].name

        override fun getCount(): Int = categories.size
    }

    companion object {

        fun newInstance(): MenuFragment {
            val args = Bundle()
            val fragment = MenuFragment()
            fragment.arguments = args
            return fragment
        }
    }
}