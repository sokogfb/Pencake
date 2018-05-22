package com.timotiusoktorio.pencake.ui.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.app.DialogFragment
import android.os.Bundle
import org.jetbrains.anko.bundleOf

class AlertDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val title = arguments!!.getString(ARG_DIALOG_TITLE)
        val message = arguments!!.getString(ARG_DIALOG_MESSAGE)
        return AlertDialog.Builder(activity)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok) { _, _ -> dismiss() }
                .create()
    }

    companion object {

        private const val ARG_DIALOG_TITLE = "ARG_DIALOG_TITLE"
        private const val ARG_DIALOG_MESSAGE = "ARG_DIALOG_MESSAGE"

        fun newInstance(title: String, message: String): AlertDialogFragment {
            val args = bundleOf(ARG_DIALOG_TITLE to title, ARG_DIALOG_MESSAGE to message)
            val fragment = AlertDialogFragment()
            fragment.arguments = args
            return fragment
        }
    }
}