package net.formula97.stacktask.logic

import net.formula97.stacktask.misc.AppConstants

enum class TaskOrder(val rawValue: Int) {
    ByDueDate(AppConstants.ORDER_BY_DUE_DATE),
    ByPriority(AppConstants.ORDER_BY_PRIORIRY),
    ByName(AppConstants.ORDER_BY_NAME)
}