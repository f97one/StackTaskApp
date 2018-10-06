package net.formula97.stacktask.logic

interface PreferenceLogic {
    fun getTaskOrder(): Int
    fun isShowConfirmDialog(): Boolean

    fun putTaskOrder(taskOrderFlag: Int)
    fun putShowConfirmDialogFlag(flag: Boolean)
}