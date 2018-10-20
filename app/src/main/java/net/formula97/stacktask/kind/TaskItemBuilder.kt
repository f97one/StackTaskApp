package net.formula97.stacktask.kind

import com.google.firebase.auth.FirebaseUser
import net.formula97.stacktask.misc.AppConstants
import java.text.SimpleDateFormat
import java.util.*

class TaskItemBuilder(taskName: String) {

    private var taskItem: TaskItem

    init {
        taskItem = TaskItem()
        taskItem.taskName = taskName
    }

    fun createAsDefault(user: FirebaseUser): TaskItem {
        userId(user)
        val cal = Calendar.getInstance()

        createdAt(cal.time)
        updatedAt(cal.time)

        cal.add(Calendar.DAY_OF_MONTH, 7)
        dueDate(cal.time)

        return taskItem
    }

    fun build(): TaskItem {
        return taskItem
    }

    fun taskId(taskId: String): TaskItemBuilder {
        taskItem.taskId = taskId

        return this
    }

    fun userId(userId: String): TaskItemBuilder {
        taskItem.userId = userId

        return this
    }

    fun userId(user: FirebaseUser): TaskItemBuilder {
        taskItem.userId = user.uid

        return this
    }

    fun taskName(taskName: String): TaskItemBuilder {
        taskItem.taskName = taskName

        return this
    }

    fun dueDate(date: Date): TaskItemBuilder {
        taskItem.dueDate = date.time

        return this
    }

    fun dueDate(dateString: String): TaskItemBuilder {
        val sdf = SimpleDateFormat(AppConstants.APP_STANDARD_DATETIME_FORMAT, Locale.getDefault())
        taskItem.dueDate = sdf.parse(dateString).time

        return this
    }

    fun priority(priority: Int): TaskItemBuilder {
        taskItem.priority = priority

        return this
    }

    fun taskDetail(taskDetail: String): TaskItemBuilder {
        taskItem.taskDetail = taskDetail

        return this
    }

    fun finished(finished: Boolean): TaskItemBuilder {
        taskItem.finished = finished

        return this
    }

    fun createdAt(createdAt: Date): TaskItemBuilder {
        taskItem.createdAt = createdAt.time

        return this
    }

    fun createdAt(createdAt: Long): TaskItemBuilder {
        taskItem.createdAt = createdAt

        return this
    }

    fun updatedAt(updatedAt: Date): TaskItemBuilder {
        taskItem.updatedAt = updatedAt.time

        return this
    }

    fun updatedAt(updatedAt: Long): TaskItemBuilder {
        taskItem.updatedAt = updatedAt

        return this
    }

    private fun getDateAsString(date: Date): String {
        val dateFormat = SimpleDateFormat(AppConstants.APP_STANDARD_DATETIME_FORMAT, Locale.getDefault())
        return dateFormat.format(date)
    }
}