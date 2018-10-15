package net.formula97.stacktask.activity

import android.os.Bundle
import net.formula97.stacktask.R
import net.formula97.stacktask.kind.TaskItem

class TaskEditorActivity : AbstractAppActivity() {

    private var receivedTaskItem: TaskItem? = null

    private val taskItemKey = "taskItemKey"

    companion object {
        const val EXTRA_TASK_ITEM: String = "EXTRA_TASK_ITEM"
    }

    override fun inflateLayout() {
        setContentView(R.layout.activity_task_editor)
    }

    override fun onCreateImpl(savedInstanceState: Bundle?) {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onResumeImpl() {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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
        outState!!.putSerializable(taskItemKey, receivedTaskItem)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        receivedTaskItem = savedInstanceState!!.getSerializable(taskItemKey) as TaskItem?
    }
}
