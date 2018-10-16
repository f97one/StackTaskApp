package net.formula97.stacktask.activity

import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_task_editor.*
import net.formula97.stacktask.R
import net.formula97.stacktask.kind.TaskItem
import net.formula97.stacktask.kind.TaskItemBuilder

class TaskEditorActivity : AbstractAppActivity() {

    private var receivedTaskItem: TaskItem? = null
    private lateinit var editorTaskItem: TaskItem
    private var hideDone: Boolean = false

    private val taskItemKey = "taskItemKey"
    private val hideDoneKey = "hideDoneKey"

    companion object {
        const val EXTRA_TASK_ITEM: String = "EXTRA_TASK_ITEM"
    }

    override fun inflateLayout() {
        setContentView(R.layout.activity_task_editor)
    }

    override fun onCreateImpl(savedInstanceState: Bundle?) {
        if (receivedTaskItem == null) {
            editorTaskItem = TaskItemBuilder("").createAsDefault(firebaseLogic.getCurrentUser()!!)
            hideDone = true
        } else {
            editorTaskItem = receivedTaskItem as TaskItem
            hideDone = false
        }

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
    }

    override fun initToolBar() {
        super.initToolBar()

        receivedTaskItem = intent.getSerializableExtra(EXTRA_TASK_ITEM) as TaskItem?

        if (receivedTaskItem == null) {
            supportActionBar!!.title = getString(R.string.add_task)
        } else {
            supportActionBar!!.title = getString(R.string.edit_task)
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
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        editorTaskItem = savedInstanceState!!.getSerializable(taskItemKey) as TaskItem
        hideDone = savedInstanceState.getBoolean(hideDoneKey)
    }
}
