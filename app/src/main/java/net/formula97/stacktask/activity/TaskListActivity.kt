package net.formula97.stacktask.activity

import android.os.Bundle
import android.support.v4.view.GravityCompat
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_task_list.*
import kotlinx.android.synthetic.main.app_navigation_drawer.*
import net.formula97.stacktask.R


class TaskListActivity : AbstractAppActivity() {

    override fun onResumeImpl() {
        val uid = firebaseLogic.getCurrentUser()!!.uid
        val itemList = firebaseLogic.readTasks(uid)
    }

    override fun onCreateImpl(savedInstanceState: Bundle?) {
        add_task_btn.setOnClickListener { _ ->
            // タスク編集画面を出す

        }

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
                Log.d("ToolBar#onItemClick", "Homeボタンが押された")
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
}
