package net.formula97.stacktask.repository

interface PreferenceRepository {
    fun getTaskOrder(): Int
    fun isShowConfirmDialog(): Boolean

    fun putTaskOrder(taskOrderFlag: Int)
    fun putShowConfirmDialogFlag(flag: Boolean)
}