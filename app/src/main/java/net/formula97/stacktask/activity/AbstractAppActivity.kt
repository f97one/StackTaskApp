package net.formula97.stacktask.activity

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.os.PersistableBundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.app_navigation_drawer.*
import kotlinx.android.synthetic.main.nav_header_main.*
import kotlinx.android.synthetic.main.app_toolbar_layout.*
import net.formula97.stacktask.R
import net.formula97.stacktask.logic.FirebaseLogic
import net.formula97.stacktask.logic.impl.FirebaseLogicImpl
import net.formula97.stacktask.repository.impl.FirebaseRepositoryImpl

abstract class AbstractAppActivity: AppCompatActivity() {

    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle

    private lateinit var firebaseLogic: FirebaseLogic

    /**
     * 画面遷移フラグ
     */
    private var permitViewFlip: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        inflateLayout()

        firebaseLogic = FirebaseLogicImpl(FirebaseRepositoryImpl(), applicationContext)

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

        app_drawer_layout.openDrawer(GravityCompat.START)

        // ヘッダに名前と画像を追加
        val currentUser = getUser()

        val viewHeader: View = app_navigation_view.getHeaderView(0)
        val userName: TextView = viewHeader.findViewById(R.id.user_name)
        val userEmail: TextView = viewHeader.findViewById(R.id.user_email)
        val userImage: ImageView = viewHeader.findViewById(R.id.user_image_view)

        if (currentUser != null) {
            userName.text = currentUser.displayName
            userEmail.text = currentUser.email
            userImage.setImageURI(currentUser.photoUrl)
        } else {
            userName.text = ""
            userEmail.text = ""
            userImage.setImageResource(R.mipmap.ic_launcher)
        }

        // 選択イベント処理
        app_navigation_view.setNavigationItemSelectedListener { menuItem ->
            var result = false

            when (menuItem.itemId) {
                R.id.nav_task_list -> {
                    Log.d("NavigationDrawer", "R.id.nav_task_list clicked")
                    // タスクリスト画面を前に出す
                    val i1 = Intent(applicationContext, TaskListActivity::class.java)
                    i1.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                    startActivity(i1)
                    result = true
                }
                R.id.nav_setting -> {
                    Log.d("NavigationDrawer", "R.id.nav_setting clicked")
                    // 設定画面を開く
                    result = true
                }
                R.id.nav_logout -> {
                    Log.d("NavigationDrawer", "R.id.nav_logout clicked")
                    // ログアウト要求を出す
                    val callback: FirebaseLogic.OnSignInFinishedListener = object: FirebaseLogic.OnSignInFinishedListener {
                        override fun onSuccess(loggedUser: FirebaseUser?) {
                            Log.d("NavigationDrawer", "logout successfully finished.")

                            val i3 = Intent(applicationContext, LoginActivity::class.java)
//                            i3.flags = Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY
                            startActivity(i3)
                        }

                        override fun onFailure(reasonException: ApiException) {
                        }
                    }

                    firebaseLogic.logout(callback)
                    result = true
                }
            }

            app_drawer_layout.closeDrawer(GravityCompat.START)
            return@setNavigationItemSelectedListener result
        }
    }

    /**
     * ログイン中のユーザー情報を返す。
     */
    fun getUser(): FirebaseUser? {
        return firebaseLogic.getCurrentUser()
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