package net.formula97.stacktask.activity

import android.os.Bundle
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_app_config.*
import net.formula97.stacktask.R
import net.formula97.stacktask.misc.AppConstants

class AppConfigActivity : AbstractAppActivity() {
    override fun inflateLayout() {
        setContentView(R.layout.activity_app_config)
    }

    override fun onCreateImpl(savedInstanceState: Bundle?) {

        val adapter = ArrayAdapter.createFromResource(this, R.array.task_order_menu, R.layout.spinner_item)
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
        task_order_by.adapter = adapter
    }

    override fun onResumeImpl() {

        show_confirm_switch.isChecked = preferenceLogic.isShowConfirmDialog()

        when (preferenceLogic.getTaskOrder()) {
            AppConstants.ORDER_BY_DUE_DATE -> task_order_by.setSelection(AppConstants.POSITION_DUE_DATE, false)
            AppConstants.ORDER_BY_PRIORITY -> task_order_by.setSelection(AppConstants.POSITION_PRIORITY, false)
            AppConstants.ORDER_BY_NAME -> task_order_by.setSelection(AppConstants.POSITION_NAME, false)
        }

    }

    override fun onPause() {
        super.onPause()

        preferenceLogic.putShowConfirmDialogFlag(show_confirm_switch.isChecked)

        val label = task_order_by.selectedItem as String
        val orderBy: Int = when (label) {
            getString(R.string.due_date) -> AppConstants.ORDER_BY_DUE_DATE
            getString(R.string.priority) -> AppConstants.ORDER_BY_PRIORITY
            getString(R.string.name) -> AppConstants.ORDER_BY_NAME
            else -> AppConstants.ORDER_BY_DUE_DATE
        }
        preferenceLogic.putTaskOrder(orderBy)
    }
}
