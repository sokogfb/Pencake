package com.timotiusoktorio.pencake.ui.profile

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.timotiusoktorio.pencake.data.model.User
import com.timotiusoktorio.pencake.data.source.DataManager

class ProfileViewModel(private val dataManager: DataManager) : ViewModel() {

    val userLiveData = MutableLiveData<User>()
    val updatePassSuccessFlagLiveData = MutableLiveData<Boolean>()
    val updatePassErrorMsgLiveData = MutableLiveData<String>()
    val signOutSuccessFlagLiveData = MutableLiveData<Boolean>()

    init {
        loadUser()
    }

    private fun loadUser() {
        dataManager.fetchUser(object : DataManager.SingleCallback<User> {
            override fun onSuccess(data: User) {
                userLiveData.value = data
            }

            override fun onError(errorMsg: String) {
                // Not implemented
            }
        })
    }

    fun updateUser(name: String, phone: String) {
        userLiveData.value?.let {
            it.displayName = name
            it.phone = phone
            dataManager.updateUser(it)
        }
    }

    fun updatePassword(oldPassword: String, newPassword: String) {
        dataManager.updatePassword(oldPassword, newPassword, object : DataManager.SingleCallback<Boolean> {
            override fun onSuccess(data: Boolean) {
                updatePassSuccessFlagLiveData.value = data
            }

            override fun onError(errorMsg: String) {
                updatePassErrorMsgLiveData.value = errorMsg
            }
        })
    }

    fun signOut() {
        FirebaseAuth.getInstance().signOut()
        signOutSuccessFlagLiveData.value = true
    }
}