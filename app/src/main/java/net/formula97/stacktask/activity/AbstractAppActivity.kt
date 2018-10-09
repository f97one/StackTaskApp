package net.formula97.stacktask.activity

import android.content.Intent
import android.os.Bundle
import android.support.v4.view.GravityCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.app_navigation_drawer.*
import kotlinx.android.synthetic.main.app_toolbar_layout.*
import net.formula97.stacktask.R
import net.formula97.stacktask.logic.FirebaseLogic
import net.formula97.stacktask.logic.PreferenceLogic
import net.formula97.stacktask.logic.impl.FirebaseLogicImpl
import net.formula97.stacktask.logic.impl.PreferenceLogicImpl
import net.formula97.stacktask.repository.impl.FirebaseRepositoryImpl

abstract class AbstractAppActivity: AppCompatActivity() {

    /**
     * Firebaseまわりのビジネスロジック、参照のみ可。
     */
    lateinit var firebaseLogic: FirebaseLogic private set

    lateinit var preferenceLogic: PreferenceLogic private set

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        inflateLayout()

        firebaseLogic = FirebaseLogicImpl(FirebaseRepositoryImpl(), applicationContext)
        preferenceLogic = PreferenceLogicImpl(this)

        initToolBar()
        initDrawer()

        onCreateImpl(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()

        onResumeImpl()
    }

    /**
     * NavigationDrawerを初期化する
     */
    private fun initDrawer() {
        // ヘッダに名前と画像を追加
        val currentUser = getUser()

        val viewHeader: View = app_navigation_view.getHeaderView(0)
        val userName: TextView = viewHeader.findViewById(R.id.user_name)
        val userEmail: TextView = viewHeader.findViewById(R.id.user_email)
        val userImage: ImageView = viewHeader.findViewById(R.id.user_image_view)

        if (currentUser != null) {
            userName.text = currentUser.displayName
            userEmail.text = currentUser.email

            // アイコンイメージを読み込む
            Glide.with(this).load(currentUser.photoUrl).into(userImage)
        } else {
            userName.text = ""
            userEmail.text = ""
            userImage.setImageResource(R.mipmap.ic_launcher)
        }

        // 選択イベント処理
        app_navigation_view.setNavigationItemSelectedListener { menuItem ->
            var result = false
            // 先にドロワーを閉じる
            app_drawer_layout.closeDrawer(GravityCompat.START)

            when (menuItem.itemId) {
                R.id.nav_task_list -> {
                    // タスクリスト画面を前に出す
                    val i1 = Intent(applicationContext, TaskListActivity::class.java)
                    i1.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                    startActivity(i1)
                    result = true
                }
                R.id.nav_setting -> {
                    // 設定画面を開く
                    val i2 = Intent(applicationContext, AppConfigActivity::class.java)
                    startActivity(i2)
                    result = true
                }
                R.id.nav_logout -> {
                    // ログアウト要求を出す
                    val callback: FirebaseLogic.OnSignInFinishedListener = object: FirebaseLogic.OnSignInFinishedListener {
                        override fun onSuccess(loggedUser: FirebaseUser?) {
                            val i3 = Intent(applicationContext, LoginActivity::class.java)
                            i3.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                            startActivity(i3)
                        }

                        override fun onFailure(reasonException: ApiException) {
                        }
                    }

                    firebaseLogic.logout(callback)
                    result = true
                }
            }

            return@setNavigationItemSelectedListener result
        }
    }

    /**
     * ToolBar初期化
     */
    open fun initToolBar() {
        setSupportActionBar(app_toolbar)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setHomeButtonEnabled(true)

        // Upナビゲーションをアイコン(=Home)ボタンとしてふるまわせる
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    /**
     * ログイン中のユーザー情報を返す。
     */
    fun getUser(): FirebaseUser? {
        return firebaseLogic.getCurrentUser()
    }

    override fun onStop() {
        super.onStop()

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