package com.timotiusoktorio.pencake.ui.profile;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.timotiusoktorio.pencake.R;
import com.timotiusoktorio.pencake.data.model.User;
import com.timotiusoktorio.pencake.databinding.FragmentProfileBinding;
import com.timotiusoktorio.pencake.ui.BaseActivity;
import com.timotiusoktorio.pencake.ui.MainActivity;
import com.timotiusoktorio.pencake.ui.dialogs.AlertDialogFragment;
import com.timotiusoktorio.pencake.ui.dialogs.ConfirmationDialogFragment;

import javax.inject.Inject;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class ProfileFragment extends Fragment implements ConfirmationDialogFragment.OnConfirmationDialogPositive {

    @Inject ProfileViewModelFactory vmFactory;

    private FragmentProfileBinding binding;
    private ProfileViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((BaseActivity) requireActivity()).getComponent().inject(this);
        requireActivity().setTitle(R.string.label_profile);

        viewModel = ViewModelProviders.of(this, vmFactory).get(ProfileViewModel.class);
        viewModel.userLiveData.observe(this, new Observer<User>() {
            @Override
            public void onChanged(@Nullable User user) {
                setUser(user);
            }
        });
        viewModel.updatePassSuccessFlagLiveData.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean resetPassSuccessFlag) {
                onResetPassSuccess(resetPassSuccessFlag);
            }
        });
        viewModel.updatePassErrorMsgLiveData.observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String errorMsg) {
                onResetPassError(errorMsg);
            }
        });
        viewModel.signOutSuccessFlagLiveData.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean signOutSuccessFlag) {
                onSignOutSuccess(signOutSuccessFlag);
            }
        });

        setOnEditorActionListenerToTextFields(binding.nameTextField);
        setOnEditorActionListenerToTextFields(binding.phoneTextField);
        setOnEditorActionListenerToTextFields(binding.newPasswordTextField);

        binding.updatePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String oldPassword = binding.oldPasswordTextField.getText().toString();
                String newPassword = binding.newPasswordTextField.getText().toString();
                if (!oldPassword.isEmpty() && !newPassword.isEmpty()) {
                    viewModel.updatePassword(oldPassword, newPassword);
                }
            }
        });
        binding.signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSignOutConfirmationDialog();
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        String name = binding.nameTextField.getText().toString();
        String phone = binding.phoneTextField.getText().toString();
        viewModel.updateUser(name, phone);
    }

    @Override
    public void onPositiveButtonClick() {
        viewModel.signOut();
    }

    private void setOnEditorActionListenerToTextFields(EditText editText) {
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                        v.clearFocus();
                        binding.mainContainer.requestFocus(); // direct the focus to the parent view.
                        return true;
                    }
                }
                return false;
            }
        });
    }

    private void setUser(@Nullable User user) {
        if (user != null) {
            binding.setUser(user);
        }
    }

    private void onResetPassSuccess(@Nullable Boolean resetPassSuccessFlag) {
        if (resetPassSuccessFlag != null) {
            Snackbar.make(binding.rootContainer, R.string.message_update_password_success, Snackbar.LENGTH_LONG).show();
            binding.oldPasswordTextField.setText("");
            binding.newPasswordTextField.setText("");
        }
    }

    private void onResetPassError(@Nullable String errorMsg) {
        if (errorMsg != null) {
            String dialogTitle = getString(R.string.dialog_title_update_password_error);
            AlertDialogFragment dialogFragment = AlertDialogFragment.newInstance(dialogTitle, errorMsg);
            dialogFragment.show(requireActivity().getFragmentManager(), "alertDialog");
            binding.oldPasswordTextField.setText("");
            binding.newPasswordTextField.setText("");
        }
    }

    private void onSignOutSuccess(@Nullable Boolean signOutSuccessFlag) {
        if (signOutSuccessFlag != null) {
            Intent signOutBroadcastIntent = new Intent(MainActivity.ACTION_SIGN_OUT);
            LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(signOutBroadcastIntent);
        }
    }

    private void showSignOutConfirmationDialog() {
        ConfirmationDialogFragment confirmationDialogFragment = ConfirmationDialogFragment.newInstance(R.string.dialog_confirmation_sign_out, R.string.label_sign_out);
        confirmationDialogFragment.setTargetFragment(this, 100);
        confirmationDialogFragment.show(getFragmentManager(), "signOutConfirmationDialog");
    }

    public static ProfileFragment newInstance() {
        Bundle args = new Bundle();
        ProfileFragment fragment = new ProfileFragment();
        fragment.setArguments(args);
        return fragment;
    }
}