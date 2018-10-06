package net.formula97.stacktask.repository.impl

import android.content.Context
import android.content.SharedPreferences
import net.formula97.stacktask.misc.AppConstants
import net.formula97.stacktask.repository.PreferenceRepository

class PreferenceRepositoryImpl(private val context: Context): PreferenceRepository {
    private val appPreferenceKey: String = "APP_PREFERENCE_KEY"
    private val taskOrderKey: String = "TASK_ORDER_KEY"
    private val showConfirmFlagKey: String = "SHOW_CONFIRM_FLAG_KEY"

    private val preference: SharedPreferences = context.getSharedPreferences(appPreferenceKey, Context.MODE_PRIVATE)

    override fun getTaskOrder(): Int {
        return preference.getInt(taskOrderKey, AppConstants.ORDER_BY_DUE_DATE)
    }

    override fun isShowConfirmDialog(): Boolean {
        return preference.getBoolean(showConfirmFlagKey, true)
    }

    override fun putTaskOrder(taskOrderFlag: Int) {
        val editor = preference.edit()
        editor.putInt(taskOrderKey, taskOrderFlag)
        editor.apply()
    }

    override fun putShowConfirmDialogFlag(flag: Boolean) {
        val editor = preference.edit()
        editor.putBoolean(showConfirmFlagKey, flag)
        editor.apply()
    }

}