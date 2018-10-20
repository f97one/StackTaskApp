package net.formula97.stacktask.kind

import net.formula97.stacktask.misc.AppConstants
import java.io.Serializable
import java.util.*

/**
 * タスクアイテム
 */
data class TaskItem(var taskName: String = "") : Serializable {
    /**
     * タスクID
     */
    var taskId: String = UUID.randomUUID().toString()
    /**
     * 保有者のユーザーID
     */
    var userId: String? = null
    /**
     * 期日
     */
    var dueDate: Long = Calendar.getInstance().timeInMillis
    /**
     * 優先度
     */
    var priority: Int = AppConstants.PRIORITY_DEFAULT
    /**
     * タスク詳細
     */
    var taskDetail: String = ""
    /**
     * 完了チェック
     */
    var finished: Boolean = false
    /**
     * 作成日
     */
    var createdAt: Long = Calendar.getInstance().timeInMillis
    /**
     * 更新日
     */
    var updatedAt: Long = Calendar.getInstance().timeInMillis

}