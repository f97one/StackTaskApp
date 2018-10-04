package net.formula97.stacktask.kind

import net.formula97.stacktask.misc.AppConstants
import org.hamcrest.Matchers.*
import org.junit.Test

import org.junit.Assert.*
import java.text.SimpleDateFormat
import java.util.*

class TaskItemBuilderTest {

    @Test
    fun createAsDefault() {
        val taskBuilder = TaskItemBuilder("task1")
        val mockUser = FirebaseUserMock()

        val result = taskBuilder.createAsDefault(mockUser)

        assertThat(result.userId, `is`("user1@example.com"))
        assertThat(result.taskName, `is`("task1"))
        assertThat(result.priority, `is`(1))
        assertThat(result.taskDetail, `is`(""))
        assertThat(result.finished, `is`(false))

        val cal1 = Calendar.getInstance();
        val sdf = SimpleDateFormat(AppConstants.APP_STANDARD_DATETIME_FORMAT, Locale.getDefault())
        cal1.add(Calendar.DAY_OF_MONTH, 7)
        val dueDate = sdf.format(cal1.time)

        assertThat(result.dueDate, startsWith(dueDate))

        val current: Long = Date().time

        // 1秒未満ならOKとする
        assertThat(current - result.createdAt!!.time, lessThan(1000L))
        assertThat(current - result.updatedAt!!.time, lessThan(1000L))
    }

    @Test
    fun buildPass1() {
        val taskBuilder = TaskItemBuilder("")

        // タスクID
        val testTaskId = UUID.randomUUID().toString()
        val d = Date()

        val sdf = SimpleDateFormat(AppConstants.APP_STANDARD_DATETIME_FORMAT, Locale.getDefault())
        val dueDate = sdf.format(d)

        taskBuilder.userId(FirebaseUserMock())
                .taskId(testTaskId)
                .taskName("task2")
                .dueDate(d)
                .priority(2)
                .taskDetail("タスクの詳細が入る")
                .finished(true)
                .createdAt(d)
                .updatedAt(d)

        val result = taskBuilder.build()

        assertThat(result.userId, `is`("user1@example.com"))
        assertThat(result.taskName, `is`("task2"))
        assertThat(result.priority, `is`(2))
        assertThat(result.taskDetail, `is`("タスクの詳細が入る"))
        assertThat(result.finished, `is`(true))
        assertThat(result.taskId, `is`(testTaskId))
        assertThat(result.dueDate, startsWith(dueDate))
        assertThat(d.time - result.createdAt!!.time, lessThan(1000L))
        assertThat(d.time - result.updatedAt!!.time, lessThan(1000L))
    }

    @Test
    fun buildPass2() {
        val taskBuilder = TaskItemBuilder("")

        // タスクID
        val testTaskId = UUID.randomUUID().toString()
        val dd = "20180930153924"
        val cal = Calendar.getInstance()
        val created = cal.time
        cal.add(Calendar.DAY_OF_MONTH, 3)
        val updated = cal.time

        taskBuilder.userId(FirebaseUserMock())
                .taskId(testTaskId)
                .taskName("task3")
                .dueDate(dd)
                .priority(2)
                .taskDetail("タスクの詳細が入る")
                .finished(true)
                .createdAt(created)
                .updatedAt(updated)

        val result = taskBuilder.build()

        assertThat(result.userId, `is`("user1@example.com"))
        assertThat(result.taskName, `is`("task3"))
        assertThat(result.priority, `is`(2))
        assertThat(result.taskDetail, `is`("タスクの詳細が入る"))
        assertThat(result.finished, `is`(true))
        assertThat(result.taskId, `is`(testTaskId))
        assertThat(result.dueDate, startsWith(dd))
        assertThat(created.time - result.createdAt!!.time, lessThan(1000L))
        assertThat(updated.time - result.updatedAt!!.time, lessThan(1000L))
    }
}