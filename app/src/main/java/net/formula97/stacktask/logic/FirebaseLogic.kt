package net.formula97.stacktask.logic

import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import net.formula97.stacktask.kind.TaskItem
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import java.lang.Exception
import java.util.*

interface FirebaseLogic {

    interface OnSignInFinishedListener: EventListener {
        fun onSuccess(loggedUser: FirebaseUser?)
        fun onFailure(reasonException: ApiException)
    }

    interface OnSubmitFinishedListener: EventListener {
        fun onSuccess(submitType: String)
        fun onFailure(submitType: String, exception: Exception)
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

    fun logout(callback: FirebaseLogic.OnSignInFinishedListener)

    fun readTasks(uid: String, orderBy: Int): List<TaskItem>

    fun addTask(taskItem: TaskItem)
    fun addTask(taskItem: TaskItem, callback: OnSubmitFinishedListener)

    fun updateTask(taskItem: TaskItem)
    fun updateTask(taskItem: TaskItem, callback: OnSubmitFinishedListener)

    fun changeOrder(taskList: List<TaskItem>, orderBy: Int): MutableList<TaskItem>

    fun getReference(): DatabaseReference
}