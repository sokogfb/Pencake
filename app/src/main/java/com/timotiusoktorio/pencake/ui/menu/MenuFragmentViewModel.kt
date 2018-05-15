package com.timotiusoktorio.pencake.ui.menu

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.timotiusoktorio.pencake.data.model.Category
import com.timotiusoktorio.pencake.data.model.KState
import com.timotiusoktorio.pencake.data.source.DataManager

class MenuFragmentViewModel(private val dataManager: DataManager) : ViewModel() {

    val categoriesLiveData = MutableLiveData<List<Category>>()
    val stateLiveData = MutableLiveData<KState>()

    init {
        loadCategories()
    }

    private fun loadCategories() {
        stateLiveData.value = KState.LOADING
        dataManager.fetchCategories(object : DataManager.Callback<Category> {
            override fun onSuccess(data: MutableList<Category>) {
                categoriesLiveData.value = data
                stateLiveData.value = KState.SUCCESS
            }

            override fun onError(errorMsg: String) {
                stateLiveData.value = KState.ERROR
            }
        })
    }
}