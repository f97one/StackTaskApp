package net.formula97.stacktask.fragment

import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import java.util.*

class MsgDialogFragment: DialogFragment() {
    companion object {
        const val DIALOG_TAG: String = "net.formula97.stacktask.fragment.MsgDialogFragment.DIALOG_TAG"

        private val msgBundle = "msg"
        private val titleBundle = "title"

        fun getInstance(msg: String, title: String): MsgDialogFragment {
            val me = MsgDialogFragment()

            val args: Bundle = Bundle()
            args.putString(msgBundle, msg)
            args.putString(titleBundle, title)
            me.arguments = args

            return me
        }
    }

    interface OnDialogButtonClickListener: EventListener {
        fun onDialogButtonClick(which: Int)
    }

    private lateinit var buttonListener: OnDialogButtonClickListener

    fun setButtonListener(buttonClickListener: OnDialogButtonClickListener) {
        this.buttonListener = buttonClickListener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())

        val args = arguments

        if (args != null) {
            builder.setTitle(args.getString(titleBundle)).setMessage(args.getString(msgBundle))

            builder.setNegativeButton(android.R.string.no) { dialog, which ->
                buttonListener.onDialogButtonClick(which)
                dialog.dismiss()
            }

            builder.setPositiveButton(android.R.string.yes) { dialog, which ->
                buttonListener.onDialogButtonClick(which)
                dialog.dismiss()
            }
        }

        return builder.create()
    }
}