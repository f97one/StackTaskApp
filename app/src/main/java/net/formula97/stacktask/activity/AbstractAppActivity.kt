package net.formula97.stacktask.activity

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.app_navigation_drawer.*
import kotlinx.android.synthetic.main.app_toolbar_layout.*
import net.formula97.stacktask.R
import net.formula97.stacktask.view.adapter.DrawerItemAdapter

abstract class AbstractAppActivity: AppCompatActivity() {

    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle

    /**
     * 画面遷移フラグ
     */
    private var permitViewFlip: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        inflateLayout()
        initDrawer()

        onCreateImpl(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        permitViewFlip = true

        onResumeImpl()
    }

    /**
     * NavigationDrawerを初期化する
     */
    private fun initDrawer() {
        // NavigationDrawer 生成の前に ToolBarを初期化
        setSupportActionBar(app_toolbar)

        // ユーザー名
        val user = getUser()
        if (user != null) user_name.text = user.displayName else {
            user_name.text = ""
        }

        // メニューを張る
        val itemList: MutableList<String> = resources.getStringArray(R.array.drawer_menu).toMutableList()
        app_drawer_item_list.adapter = DrawerItemAdapter(this, R.layout.drawer_list_item, itemList)

        app_drawer_item_list.onItemClickListener = AdapterView.OnItemClickListener { adapterView: AdapterView<*>, parent: View, position: Int, id: Long ->
            when (position) {
                0 -> {
                    // 選択位置 = 0 -> タスクリスト画面へ移動
                    val i0 = Intent(this, TaskListActivity::class.java)
                    i0.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                    startActivity(i0)
                }
                1 -> {
                    // 選択位置 = 1 -> アプリ設定画面へ移動
                    // TODO アプリ設定画面のクラスを作った後に実装する
                }
                2 -> {
                    // 選択位置 = 2 -> ログアウト
                    requestLogout()
                }
                else -> {
                    // それ以外の場合は不正操作の可能性ありのためログアウトさせる
                    requestLogout()
                }
            }
        }

        // 開閉イベントの処理
        actionBarDrawerToggle = object : ActionBarDrawerToggle(this, app_drawer, R.string.drawer_open, R.string.drawer_close) {
            override fun onDrawerClosed(drawerView: View) {
                super.onDrawerClosed(drawerView)
            }

            override fun onDrawerOpened(drawerView: View) {
                super.onDrawerOpened(drawerView)
            }
        }

        app_drawer.addDrawerListener(actionBarDrawerToggle)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeButtonEnabled(true)
    }

    /**
     * ログイン中のユーザー情報を返す。
     */
    fun getUser(): FirebaseUser? {
        return FirebaseAuth.getInstance().currentUser
    }

    /**
     * ログアウト要求を送る。
     */
    fun requestLogout() {
        // TODO ログアウト要求の処理を書く
    }

    override fun onPostCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onPostCreate(savedInstanceState, persistentState)

        // ActionBarの状態を同期する
        actionBarDrawerToggle.syncState()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        // 開閉状態の同期
        actionBarDrawerToggle.onConfigurationChanged(newConfig)
    }

    override fun onStop() {
        super.onStop()

        app_drawer.removeDrawerListener(actionBarDrawerToggle)
    }

    override fun startActivity(intent: Intent?) {
        if (permitViewFlip) {
            permitViewFlip = false
            super.startActivity(intent)
        }
    }

    override fun startActivityForResult(intent: Intent?, requestCode: Int) {
        if (permitViewFlip) {
            permitViewFlip = false
            super.startActivityForResult(intent, requestCode)
        }
    }

    /**
     * レイアウトを展開する。
     */
    abstract fun inflateLayout()

    /**
     * 各ActivityでのonCreate実装
     */
    abstract fun onCreateImpl(savedInstanceState: Bundle?)

    /**
     * 各ActivityでのonResume実装
     */
    abstract fun onResumeImpl()
}