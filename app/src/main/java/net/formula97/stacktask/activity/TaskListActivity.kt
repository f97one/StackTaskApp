package net.formula97.stacktask.activity

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
import net.formula97.stacktask.kind.TaskItem
import net.formula97.stacktask.logic.TaskOrder
import net.formula97.stacktask.misc.AppConstants
import net.formula97.stacktask.view.adapter.TaskListAdapter


class TaskListActivity : AbstractAppActivity() {

    private var taskItemList = mutableListOf<TaskItem>()
    private var taskListAdapter = TaskListAdapter(taskItemList)

    override fun onStart() {
        super.onStart()

        createTaskListView(mutableListOf())
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

    }

    private fun createTaskListView(taskList: MutableList<TaskItem>) {
        val taskOrder = when (preferenceLogic.getTaskOrder()) {
            AppConstants.ORDER_BY_DUE_DATE -> {
                TaskOrder.ByDueDate
            }
            AppConstants.ORDER_BY_PRIORIRY -> {
                TaskOrder.ByPriority
            }
            AppConstants.ORDER_BY_NAME -> {
                TaskOrder.ByName
            }
            else -> {
                TaskOrder.ByDueDate
            }
        }

        val list = firebaseLogic.changeOrder(taskList, taskOrder)

        taskListAdapter.replaceItems(list)

        taskListAdapter.setOnItemClickLister(object : TaskListAdapter.OnItemClickListener {
            override fun onItemClick(view: View, position: Int, item: TaskItem) {
                Log.d("RecyclerView#onItemClick", "アイテムを押された")
            }
        })
        taskListAdapter.setOnItemCheckedChangeListener(object : TaskListAdapter.OnItemCheckedChangeListener {
            override fun onItemCheckedChange(view: View, position: Int, checked: Boolean, item: TaskItem) {
                Log.d("RecyclerView#onItemCheckedChange", "アイテムのチェックボックスが押された")
            }
        })
        task_item_list.layoutManager = LinearLayoutManager(this)
        task_item_list.setHasFixedSize(true)

        task_item_list.adapter = taskListAdapter
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
                // TODO 並べ替え処理を書く
                Log.d("ToolBar#onItemClick", "期日で並べ替える")
                true
            }
            R.id.order_by_priority -> {
                // TODO 並べ替え処理を書く
                Log.d("ToolBar#onItemClick", "優先度で並べ替える")
                true
            }
            R.id.order_by_name -> {
                // TODO 並べ替え処理を書く
                Log.d("ToolBar#onItemClick", "名前で並べ替える")
                true
            }
            else -> {
                Log.d("ToolBar#onItemClick", "何も押されなかった")
                false
            }
        }
    }

    private var fetchCallback: ValueEventListener = object : ValueEventListener {
        override fun onCancelled(databaseError: DatabaseError) {

        }

        override fun onDataChange(dataSnapshot: DataSnapshot) {
            for (snapshot in dataSnapshot.children) {
                val i = snapshot.getValue(TaskItem::class.java)
                if (i != null) {
                    taskItemList.add(i)
                }
            }

            createTaskListView(taskItemList)
        }
    }
}
