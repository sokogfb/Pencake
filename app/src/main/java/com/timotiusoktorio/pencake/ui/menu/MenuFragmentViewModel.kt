package com.timotiusoktorio.pencake.ui.menu

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.timotiusoktorio.pencake.data.model.Category
import com.timotiusoktorio.pencake.data.model.State
import com.timotiusoktorio.pencake.data.source.DataManager

class MenuFragmentViewModel(private val dataManager: DataManager) : ViewModel() {

    val stateLiveData = MutableLiveData<State>()
    val categoriesLiveData = MutableLiveData<List<Category>>()

    init {
        loadCategories()
    }

    private fun loadCategories() {
        stateLiveData.value = State.LOADING
        dataManager.fetchCategories(object : DataManager.Callback<Category> {
            override fun onSuccess(data: List<Category>) {
                categoriesLiveData.value = data
                stateLiveData.value = State.SUCCESS
            }

            override fun onError(errorMsg: String) {
                stateLiveData.value = State.ERROR
            }
        })
    }
}