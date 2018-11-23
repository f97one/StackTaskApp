package net.formula97.stacktask.fragment

import android.app.Dialog
import android.app.ProgressDialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import net.formula97.stacktask.R

class ProgressFragment: DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val progDialog = ProgressDialog(activity)
        progDialog.setMessage(getString(R.string.signin_please_wait))
        progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER)

        return progDialog
    }

    companion object {

        const val FRAGMENT_TAG: String = "net.formula97.stacktask.fragment.ProgressFragment.DIALOG_TAG"

    }

}