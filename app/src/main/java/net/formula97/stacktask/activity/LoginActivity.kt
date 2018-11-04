package net.formula97.stacktask.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.login.*
import net.formula97.stacktask.R
import net.formula97.stacktask.logic.FirebaseLogic
import net.formula97.stacktask.logic.impl.FirebaseLogicImpl
import net.formula97.stacktask.repository.impl.FirebaseRepositoryImpl
import java.lang.Exception

class LoginActivity : AppCompatActivity() {

    private val loginRequestCode: Int = 0x8086

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)

        login_button.setOnClickListener { _ ->
            // ログインボタンをロックしてログイン処理を行う
            login_button.isEnabled = false

            val firebaseLogic = FirebaseLogicImpl(FirebaseRepositoryImpl(), applicationContext)
            val signInIntent = firebaseLogic.getGoogleSignInClient().signInIntent
            startActivityForResult(signInIntent, loginRequestCode)
        }
    }

    override fun onResume() {
        super.onResume()

        if (!login_button.isEnabled) {
            login_button.isEnabled = true
        }

        val firebaseLogic = FirebaseLogicImpl(FirebaseRepositoryImpl(), applicationContext)

        if (firebaseLogic.getCurrentUser() == null) {
            initializing.visibility = View.GONE
            login_button.visibility = View.VISIBLE
        } else {
            val intent = Intent(this, TaskListActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == loginRequestCode) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)

            val prog = ProgressFragment()
            prog.show(supportFragmentManager, ProgressFragment.FRAGMENT_TAG)

            try {
                val account = task.getResult(ApiException::class.java)
                val firebaseLogic = FirebaseLogicImpl(FirebaseRepositoryImpl(), applicationContext)
                firebaseLogic.signInWithGoogle(account!!, onSignInFinishedListener)
            } catch (e: ApiException) {
                Toast.makeText(applicationContext, R.string.google_login_failed, Toast.LENGTH_SHORT).show()

                prog.dismiss()
                login_button.isEnabled = true
            }

        }
    }

    private val onSignInFinishedListener = object: FirebaseLogic.OnSignInFinishedListener {
        override fun onSuccess(loggedUser: FirebaseUser?) {
            val prog: ProgressFragment = supportFragmentManager.findFragmentByTag(ProgressFragment.FRAGMENT_TAG) as ProgressFragment
            prog.dismiss()

            val intent = Intent(applicationContext, TaskListActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP

            startActivity(intent)
        }

        override fun onFailure(reasonException: Exception?) {
            val prog: ProgressFragment = supportFragmentManager.findFragmentByTag(ProgressFragment.FRAGMENT_TAG) as ProgressFragment
            prog.dismiss()

            login_button.isEnabled = true

            Toast.makeText(applicationContext, R.string.firebase_auth_failed, Toast.LENGTH_SHORT).show()
        }
    }
}
