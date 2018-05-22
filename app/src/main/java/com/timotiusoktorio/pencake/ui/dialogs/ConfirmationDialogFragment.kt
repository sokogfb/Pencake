package com.timotiusoktorio.pencake.ui.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.support.annotation.StringRes
import android.support.v4.app.DialogFragment
import org.jetbrains.anko.bundleOf

class ConfirmationDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val messageRes = arguments!!.getInt(ARG_MESSAGE_RES)
        val posButtonLabelRes = arguments!!.getInt(ARG_POS_BUTTON_LABEL_RES)
        return AlertDialog.Builder(activity)
                .setMessage(messageRes)
                .setPositiveButton(posButtonLabelRes) { _, _ ->
                    if (activity is OnConfirmationDialogPositive) {
                        (activity as OnConfirmationDialogPositive).onPositiveButtonClick()
                    } else if (targetFragment is OnConfirmationDialogPositive) {
                        (targetFragment as OnConfirmationDialogPositive).onPositiveButtonClick()
                    }
                }
                .setNegativeButton(android.R.string.cancel) { _, _ -> dismiss() }
                .create()
    }

    interface OnConfirmationDialogPositive {

        fun onPositiveButtonClick()
    }

    companion object {

        private const val ARG_MESSAGE_RES = "ARG_MESSAGE_RES"
        private const val ARG_POS_BUTTON_LABEL_RES = "ARG_POS_BUTTON_LABEL_RES"

        fun newInstance(@StringRes messageRes: Int, @StringRes posButtonLabelRes: Int): ConfirmationDialogFragment {
            val args = bundleOf(ARG_MESSAGE_RES to messageRes, ARG_POS_BUTTON_LABEL_RES to posButtonLabelRes)
            val fragment = ConfirmationDialogFragment()
            fragment.arguments = args
            return fragment
        }
    }
}