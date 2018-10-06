package net.formula97.stacktask.logic.impl

import android.content.Context
import net.formula97.stacktask.logic.PreferenceLogic
import net.formula97.stacktask.misc.AppConstants
import net.formula97.stacktask.repository.PreferenceRepository
import net.formula97.stacktask.repository.impl.PreferenceRepositoryImpl
import java.lang.IllegalArgumentException

class PreferenceLogicImpl(context: Context): PreferenceLogic {

    private var preferenceRepository: PreferenceRepository = PreferenceRepositoryImpl(context)

    override fun getTaskOrder(): Int {
        return preferenceRepository.getTaskOrder()
    }

    override fun isShowConfirmDialog(): Boolean {
        return preferenceRepository.isShowConfirmDialog()
    }

    override fun putTaskOrder(taskOrderFlag: Int) {
        when (taskOrderFlag) {
            AppConstants.ORDER_BY_DUE_DATE, AppConstants.ORDER_BY_PRIORIRY, AppConstants.ORDER_BY_NAME -> {
                preferenceRepository.putTaskOrder(taskOrderFlag)
            }
            else -> {
                throw IllegalArgumentException("taskOrderFlag must be one of 10, 11, 12. Passed = $taskOrderFlag")
            }
        }
    }

    override fun putShowConfirmDialogFlag(flag: Boolean) {
        preferenceRepository.putShowConfirmDialogFlag(flag)
    }

}