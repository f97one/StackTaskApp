package net.formula97.stacktask.kind

import net.formula97.stacktask.misc.AppConstants
import java.util.*

/**
 * タスクアイテム
 */
data class TaskItem(var taskName: String) {
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
    var dueDate: String? = null
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
    var createdAt: Date? = null
    /**
     * 更新日
     */
    var updatedAt: Date? = null

}