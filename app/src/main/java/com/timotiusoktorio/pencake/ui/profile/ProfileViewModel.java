package com.timotiusoktorio.pencake.ui.profile;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.timotiusoktorio.pencake.data.model.User;
import com.timotiusoktorio.pencake.data.source.DataManager;

class ProfileViewModel extends ViewModel {

    final MutableLiveData<User> userLiveData = new MutableLiveData<>();
    final MutableLiveData<Boolean> updatePassSuccessFlagLiveData = new MutableLiveData<>();
    final MutableLiveData<String> updatePassErrorMsgLiveData = new MutableLiveData<>();
    final MutableLiveData<Boolean> signOutSuccessFlagLiveData = new MutableLiveData<>();

    private final DataManager dataManager;

    ProfileViewModel(DataManager dataManager) {
        this.dataManager = dataManager;
        loadUser();
    }

    private void loadUser() {
        dataManager.fetchUser(new DataManager.SingleCallback<User>() {
            @Override
            public void onSuccess(User data) {
                userLiveData.setValue(data);
            }

            @Override
            public void onError(String errorMsg) {

            }
        });
    }

    void updateUser(String name, String phone) {
        User user = userLiveData.getValue();
        if (user != null) {
            user.setDisplayName(name);
            user.setPhone(phone);
            dataManager.updateUser(user);
        }
    }

    void updatePassword(String oldPassword, final String newPassword) {
        dataManager.updatePassword(oldPassword, newPassword, new DataManager.SingleCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean data) {
                updatePassSuccessFlagLiveData.setValue(data);
            }

            @Override
            public void onError(String errorMsg) {
                updatePassErrorMsgLiveData.setValue(errorMsg);
            }
        });
    }

    void signOut() {
        FirebaseAuth.getInstance().signOut();
        dataManager.removeFavoriteList();
        signOutSuccessFlagLiveData.setValue(true);
    }
}