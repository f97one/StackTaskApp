package net.formula97.stacktask.activity

import android.os.Bundle
import net.formula97.stacktask.R

class TaskEditorActivity : AbstractAppActivity() {

    private var receivedTaskId: String? = null

    private val taskIdKey = "taskIdKey"

    companion object {
        const val EXTRA_TASK_ID: String = "EXTRA_TASK_ID"
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

        receivedTaskId = intent.getStringExtra(EXTRA_TASK_ID)

        if (receivedTaskId == null) {
            supportActionBar!!.title = getString(R.string.add_task)
        } else {
            supportActionBar!!.title = getString(R.string.edit_task)
        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState!!.putString(taskIdKey, receivedTaskId)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        receivedTaskId = savedInstanceState!!.getString(taskIdKey)
    }
}
