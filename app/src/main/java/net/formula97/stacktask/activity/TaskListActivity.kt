package net.formula97.stacktask.activity

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.support.v4.view.GravityCompat
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_task_list.*
//import kotlinx.android.synthetic.main.app_navigation_drawer.*
import net.formula97.stacktask.R
import net.formula97.stacktask.fragment.MsgDialogFragment
import net.formula97.stacktask.kind.TaskItem
import net.formula97.stacktask.misc.AppConstants
import net.formula97.stacktask.view.adapter.TaskListAdapter
import java.util.*


class TaskListActivity : AbstractAppActivity() {

    private var taskItemList = mutableListOf<TaskItem>()

    private var tmpTaskOrder: Int = AppConstants.ORDER_BY_DUE_DATE

    override fun onStart() {
        super.onStart()

        createTaskListView()
        startFetchingData()
    }

    override fun onResumeImpl() {

    }

    private fun startFetchingData() {
        val uid = firebaseLogic.getCurrentUser()!!.uid

        val reference = firebaseLogic.getReference()
        reference.startAt(uid, "userId")
        reference.addListenerForSingleValueEvent(fetchCallback)
    }

    override fun onPause() {
        super.onPause()
        firebaseLogic.getReference().removeEventListener(fetchCallback)
    }

    override fun onCreateImpl(savedInstanceState: Bundle?) {
        add_task_btn.setOnClickListener { _ ->
            // タスク編集画面を出す
            val intent = Intent(applicationContext, TaskEditorActivity::class.java)
            startActivity(intent)
        }

        createTaskListView()

        tmpTaskOrder = preferenceLogic.getTaskOrder()
    }

    private fun createTaskListView() {
        val taskListAdapter = TaskListAdapter(taskItemList)

        taskListAdapter.setOnItemClickLister(object : TaskListAdapter.OnItemClickListener {
            override fun onItemClick(view: View, position: Int, item: TaskItem) {
                Log.d("RecyclerView#onItemClick", "アイテムを押された")
            }
        })
        taskListAdapter.setOnItemCheckedChangeListener(object : TaskListAdapter.OnItemCheckedChangeListener {
            override fun onItemCheckedChange(view: View, position: Int, checked: Boolean, item: TaskItem) {
                Log.d("RecyclerView#onItemCheckedChange", "アイテムのチェックボックスが押された")

                val showConfirm: Boolean = preferenceLogic.isShowConfirmDialog()

                if (showConfirm && checked && !item.finished) {
                    val dialog: MsgDialogFragment = MsgDialogFragment.getInstance(getString(R.string.task_will_be_completed), getString(R.string.confirm))
                    dialog.setButtonListener(object : MsgDialogFragment.OnDialogButtonClickListener {
                        override fun onDialogButtonClick(which: Int) {
                            if (which == DialogInterface.BUTTON_POSITIVE) {
                                postCompleteState(item, checked, position)
                            } else {
                                // チェックボックスの表示を戻す
                                val adapter: TaskListAdapter = task_item_list.adapter as TaskListAdapter
                                adapter.notifyItemChanged(position, item)
                            }
                        }
                    })

                    dialog.show(supportFragmentManager, MsgDialogFragment.DIALOG_TAG)

                } else {
                    postCompleteState(item, checked, position)
                }

            }

            private fun postCompleteState(item: TaskItem, checked: Boolean, position: Int) {
                item.finished = checked
                item.updatedAt = Date().time

                updateItem(position, item)
                firebaseLogic.updateTask(item)
            }
        })
        task_item_list.layoutManager = LinearLayoutManager(this)
        task_item_list.setHasFixedSize(true)

        task_item_list.adapter = taskListAdapter
    }

    private fun updateItem(position: Int, item: TaskItem) {
        val adapter: TaskListAdapter = task_item_list.adapter as TaskListAdapter
        adapter.replaceItem(position, item)
    }

    override fun inflateLayout() {
        setContentView(R.layout.activity_task_list)
    }

    override fun initToolBar() {
        super.initToolBar()

        supportActionBar!!.title = getString(R.string.task_list)
        supportActionBar!!.setHomeAsUpIndicator(R.mipmap.ic_launcher)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.task_order_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item!!.itemId) {
            android.R.id.home -> {
                app_drawer_layout.openDrawer(GravityCompat.START)
                true
            }
            R.id.order_by_due_date -> {
                Log.d("ToolBar#onItemClick", "期日で並べ替える")
                tmpTaskOrder = AppConstants.ORDER_BY_DUE_DATE
                invalidateList(tmpTaskOrder)
                true
            }
            R.id.order_by_priority -> {
                Log.d("ToolBar#onItemClick", "優先度で並べ替える")
                tmpTaskOrder = AppConstants.ORDER_BY_PRIORIRY
                invalidateList(tmpTaskOrder)
                true
            }
            R.id.order_by_name -> {
                Log.d("ToolBar#onItemClick", "名前で並べ替える")
                tmpTaskOrder = AppConstants.ORDER_BY_NAME
                invalidateList(tmpTaskOrder)
                true
            }
            else -> {
                Log.d("ToolBar#onItemClick", "何も押されなかった")
                false
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putInt("tmpTaskOrder", tmpTaskOrder)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)

        tmpTaskOrder = savedInstanceState?.getInt("tmpTaskOrder") ?: preferenceLogic.getTaskOrder()
    }

    private var fetchCallback: ValueEventListener = object : ValueEventListener {
        override fun onCancelled(databaseError: DatabaseError) {

        }

        override fun onDataChange(dataSnapshot: DataSnapshot) {
            for (snapshot in dataSnapshot.children) {
                val key = snapshot.key
                val i = snapshot.getValue(TaskItem::class.java)
                if (i != null) {
                    i.taskId = key!!
                    taskItemList.add(i)
                }
            }

            invalidateList(tmpTaskOrder)
        }
    }

    private fun invalidateList(orderBy: Int) {
        val itemList = firebaseLogic.changeOrder(taskItemList, orderBy)

        val adapter: TaskListAdapter = task_item_list.adapter as TaskListAdapter
        adapter.replaceItems(itemList)
    }
}
