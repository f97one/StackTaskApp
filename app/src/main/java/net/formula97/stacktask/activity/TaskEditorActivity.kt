package net.formula97.stacktask.activity

import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_task_editor.*
import net.formula97.stacktask.R
import net.formula97.stacktask.kind.TaskItem
import net.formula97.stacktask.kind.TaskItemBuilder
import net.formula97.stacktask.misc.AppConstants
import java.text.SimpleDateFormat
import java.util.*

class TaskEditorActivity : AbstractAppActivity() {

    private lateinit var editorTaskItem: TaskItem
    private var hideDone: Boolean = false
    private var editorCalendar: Calendar = Calendar.getInstance()

    private val taskItemKey = "taskItemKey"
    private val hideDoneKey = "hideDoneKey"
    private val editorCalendarKey = "editorCalendarKey"

    companion object {
        const val EXTRA_TASK_ITEM: String = "EXTRA_TASK_ITEM"
    }

    override fun inflateLayout() {
        setContentView(R.layout.activity_task_editor)
    }

    override fun onCreateImpl(savedInstanceState: Bundle?) {
        val btnResId: Int
        if (hideDone) {
            btnResId = android.R.drawable.ic_menu_add
            editorCalendar.add(Calendar.DAY_OF_MONTH, 7)

            val dateFormat = SimpleDateFormat(AppConstants.APP_STANDARD_DATETIME_FORMAT, Locale.getDefault())
            editorTaskItem.dueDate = dateFormat.format(editorCalendar.time)

        } else {
            btnResId = android.R.drawable.ic_menu_edit
        }

        submit_task_btn.setImageResource(btnResId)
    }

    override fun onResumeImpl() {
        editor_done.visibility = if (hideDone) {
            View.GONE
        } else {
            View.VISIBLE
        }

        editor_task_name.setText(editorTaskItem.taskName)
        editor_due_date.text = editorTaskItem.dueDate
        editor_priority.rating = editorTaskItem.priority.toFloat()
        editor_details.setText(editorTaskItem.taskDetail)
        editor_done.isChecked = editorTaskItem.finished
        current_length.text = editorTaskItem.taskDetail.length.toString()
    }

    override fun initToolBar() {
        super.initToolBar()

        val receivedTaskItem = intent.getSerializableExtra(EXTRA_TASK_ITEM) as TaskItem?

        if (receivedTaskItem == null) {
            supportActionBar!!.title = getString(R.string.add_task)
            editorTaskItem = TaskItemBuilder("").createAsDefault(firebaseLogic.getCurrentUser()!!)
            hideDone = true
        } else {
            supportActionBar!!.title = getString(R.string.edit_task)
            editorTaskItem = receivedTaskItem as TaskItem
            hideDone = false
        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)

        editorTaskItem.taskName = editor_task_name.text.toString()
        editorTaskItem.dueDate = editor_due_date.text.toString()
        editorTaskItem.priority = editor_priority.rating.toInt()
        editorTaskItem.taskDetail = editor_details.text.toString()
        editorTaskItem.finished = editor_done.isChecked

        outState!!.putSerializable(taskItemKey, editorTaskItem)
        outState.putBoolean(hideDoneKey, hideDone)
        outState.putSerializable(editorCalendarKey, editorCalendar)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        editorTaskItem = savedInstanceState!!.getSerializable(taskItemKey) as TaskItem
        hideDone = savedInstanceState.getBoolean(hideDoneKey)
        editorCalendar = savedInstanceState.getSerializable(editorCalendarKey) as Calendar
    }
}
