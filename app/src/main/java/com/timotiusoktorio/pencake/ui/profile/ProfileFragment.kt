package com.timotiusoktorio.pencake.ui.profile

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.LocalBroadcastManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.timotiusoktorio.pencake.R
import com.timotiusoktorio.pencake.data.model.User
import com.timotiusoktorio.pencake.data.source.DataManager
import com.timotiusoktorio.pencake.extensions.observe
import com.timotiusoktorio.pencake.extensions.onDoneImeAction
import com.timotiusoktorio.pencake.extensions.withViewModel
import com.timotiusoktorio.pencake.ui.BaseActivity
import com.timotiusoktorio.pencake.ui.MainActivity
import com.timotiusoktorio.pencake.ui.dialogs.AlertDialogFragment
import com.timotiusoktorio.pencake.ui.dialogs.ConfirmationDialogFragment
import kotlinx.android.synthetic.main.fragment_profile.*
import org.jetbrains.anko.design.longSnackbar
import javax.inject.Inject

class ProfileFragment : Fragment(), ConfirmationDialogFragment.OnConfirmationDialogPositive {

    @Inject lateinit var dataManager: DataManager

    private lateinit var viewModel: ProfileViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        nameTextField.onDoneImeAction { mainContainer.requestFocus() }
        phoneTextField.onDoneImeAction { mainContainer.requestFocus() }
        newPasswordTextField.onDoneImeAction { mainContainer.requestFocus() }
        updatePasswordButton.setOnClickListener { checkPasswordTextFieldsAndSubmit() }
        signOutButton.setOnClickListener { showConfirmationDialog() }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as BaseActivity).component.inject(this)
        requireActivity().setTitle(R.string.label_profile)

        viewModel = withViewModel({ ProfileViewModel(dataManager) }) {
            observe(userLiveData, ::onUserFetched)
            observe(updatePassSuccessFlagLiveData, ::onUpdatePassSuccess)
            observe(updatePassErrorMsgLiveData, ::onUpdatePassError)
            observe(signOutSuccessFlagLiveData, ::onSignOutSuccess)
        }
    }

    override fun onStop() {
        super.onStop()
        viewModel.updateUser(nameTextField.text.toString(), phoneTextField.text.toString())
    }

    override fun onPositiveButtonClick() {
        viewModel.signOut()
    }

    private fun checkPasswordTextFieldsAndSubmit() {
        val oldPassword = oldPasswordTextField.text.toString()
        val newPassword = newPasswordTextField.text.toString()
        if (oldPassword.isNotBlank() && newPassword.isNotBlank()) {
            viewModel.updatePassword(oldPassword, newPassword)
        }
    }

    private fun showConfirmationDialog() {
        val dialogFragment = ConfirmationDialogFragment.newInstance(R.string.dialog_confirmation_sign_out, R.string.label_sign_out)
        dialogFragment.setTargetFragment(this, 100)
        dialogFragment.show(fragmentManager, "signOutConfirmationDialog")
    }

    private fun onUserFetched(user: User?) {
        user?.let {
            nameTextField.setText(it.displayName)
            emailTextField.setText(it.email)
            phoneTextField.setText(it.phone)
        }
    }

    private fun onUpdatePassSuccess(updatePassSuccessFlag: Boolean?) {
        updatePassSuccessFlag?.let {
            longSnackbar(rootContainer, R.string.message_update_password_success)
            oldPasswordTextField.setText("")
            newPasswordTextField.setText("")
        }
    }

    private fun onUpdatePassError(updatePassErrorMsg: String?) {
        updatePassErrorMsg?.let {
            val dialogTitle = getString(R.string.dialog_title_update_password_error)
            val dialogFragment = AlertDialogFragment.newInstance(dialogTitle, it)
            dialogFragment.show(requireActivity().fragmentManager, "updatePassErrorDialog")
            oldPasswordTextField.setText("")
            newPasswordTextField.setText("")
        }
    }

    private fun onSignOutSuccess(signOutSuccessFlag: Boolean?) {
        signOutSuccessFlag?.let {
            LocalBroadcastManager.getInstance(requireContext())
                    .sendBroadcast(Intent(MainActivity.ACTION_SIGN_OUT))
        }
    }

    companion object {

        fun newInstance(): ProfileFragment {
            val args = Bundle()
            val fragment = ProfileFragment()
            fragment.arguments = args
            return fragment
        }
    }
}