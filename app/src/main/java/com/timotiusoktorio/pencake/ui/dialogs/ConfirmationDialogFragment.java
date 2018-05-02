package com.timotiusoktorio.pencake.ui.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.app.DialogFragment;

public class ConfirmationDialogFragment extends DialogFragment {

    private static final String ARG_MESSAGE_RES = "messageRes";
    private static final String ARG_POS_BUTTON_LABEL_RES = "posButtonLabelRes";

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int messageRes = getArguments().getInt(ARG_MESSAGE_RES);
        int posButtonLabelRes = getArguments().getInt(ARG_POS_BUTTON_LABEL_RES);
        return new AlertDialog.Builder(getActivity())
                .setMessage(messageRes)
                .setPositiveButton(posButtonLabelRes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (getActivity() instanceof OnConfirmationDialogPositive) {
                            ((OnConfirmationDialogPositive) getActivity()).onPositiveButtonClick();
                        } else if (getTargetFragment() instanceof OnConfirmationDialogPositive) {
                            ((OnConfirmationDialogPositive) getTargetFragment()).onPositiveButtonClick();
                        }
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dismiss();
                    }
                })
                .create();
    }

    public interface OnConfirmationDialogPositive {

        void onPositiveButtonClick();
    }

    public static ConfirmationDialogFragment newInstance(@StringRes int messageRes, @StringRes int posButtonLabelRes) {
        Bundle args = new Bundle();
        args.putInt(ARG_MESSAGE_RES, messageRes);
        args.putInt(ARG_POS_BUTTON_LABEL_RES, posButtonLabelRes);
        ConfirmationDialogFragment fragment = new ConfirmationDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }
}