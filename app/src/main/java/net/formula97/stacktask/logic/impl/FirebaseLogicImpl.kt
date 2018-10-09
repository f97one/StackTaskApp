package net.formula97.stacktask.logic.impl

import android.content.Context
import android.util.Log
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.DatabaseReference
import kotlinx.coroutines.experimental.launch
import net.formula97.stacktask.R
import net.formula97.stacktask.kind.TaskItem
import net.formula97.stacktask.logic.FirebaseLogic
import net.formula97.stacktask.logic.TaskOrder
import net.formula97.stacktask.repository.FirebaseRepository

class FirebaseLogicImpl constructor(private val firebaseRepository: FirebaseRepository, private val context: Context) : FirebaseLogic {
    override fun logout(callback: FirebaseLogic.OnSignInFinishedListener) {
        val auth = FirebaseAuth.getInstance()
        auth.addAuthStateListener { firebaseAuth ->
            Log.d("logout", "FirebaseAuth state changed.")

            if (firebaseAuth.currentUser == null) {
                callback.onSuccess(null)
            }
        }
        auth.signOut()
    }

    private var googleSignInClient: GoogleSignInClient? = null

    override fun getGoogleSignInClient(): GoogleSignInClient {

        if (googleSignInClient == null) {
            val gso: GoogleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(context.getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build()
            googleSignInClient = GoogleSignIn.getClient(context, gso)
        }

        return googleSignInClient!!
    }

    override fun signInWithGoogle(account: GoogleSignInAccount, callback: FirebaseLogic.OnSignInFinishedListener) {
        val cred = GoogleAuthProvider.getCredential(account.idToken, null)
        val auth = FirebaseAuth.getInstance()

        auth.signInWithCredential(cred)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        callback.onSuccess(auth.currentUser)
                    } else {
                        callback.onFailure(task.exception as ApiException)
                    }
                }
    }

    override fun getCurrentUser(): FirebaseUser? {
        return FirebaseAuth.getInstance().currentUser
    }

    override fun readTasks(uid: String, orderBy: TaskOrder): List<TaskItem> {
        val rawTaskList: MutableList<TaskItem> = firebaseRepository.readTasksOnce(uid)

        return changeOrder(rawTaskList.toList(), orderBy)
    }

    override fun addTask(taskItem: TaskItem) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateTask(taskitem: TaskItem) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun changeOrder(taskList: List<TaskItem>, orderBy: TaskOrder): MutableList<TaskItem> {
        val mutableList: MutableList<TaskItem> = mutableListOf()
        mutableList.addAll(taskList)

        when (orderBy.rawValue) {
            TaskOrder.ByDueDate.rawValue -> {
                mutableList.sortedWith(compareByDescending<TaskItem> { it.dueDate }
                        .thenByDescending { it.priority }
                        .thenBy { it.taskName }
                )
            }
            TaskOrder.ByPriority.rawValue -> {
                mutableList.sortedWith(compareByDescending<TaskItem> { it.priority }
                        .thenByDescending { it.dueDate }
                        .thenBy { it.taskName })
            }
            TaskOrder.ByName.rawValue -> {
                mutableList.sortedWith(compareBy<TaskItem> { it.taskName }
                        .thenByDescending { it.dueDate }
                        .thenByDescending { it.priority })
            }
            else -> {
                mutableList.sortedWith(compareByDescending<TaskItem> { it.dueDate }
                        .thenByDescending { it.priority }
                        .thenBy { it.taskName }
                )
            }
        }

        return mutableList
    }

    override fun getReference(): DatabaseReference {
        return firebaseRepository.getReference()
    }
}