package net.formula97.stacktask.logic

import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import net.formula97.stacktask.kind.TaskItem
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseUser
import java.util.*

interface FirebaseLogic {

    interface OnSignInFinishedListener: EventListener {
        fun onSuccess(loggedUser: FirebaseUser)
        fun onFailure(reasonException: ApiException)
    }

    /**
     * 端末のGoogleアカウントでサインインする。
     */
    fun signInWithGoogle(account: GoogleSignInAccount, callback: OnSignInFinishedListener)

    /**
     * ログオン中のユーザーを取得する。
     */
    fun getCurrentUser(): FirebaseUser?

    fun getGoogleSignInClient(): GoogleSignInClient

    fun readTasks(uid: String): List<TaskItem>
    fun addTask(taskItem: TaskItem)
    fun updateTask(taskitem: TaskItem)

}