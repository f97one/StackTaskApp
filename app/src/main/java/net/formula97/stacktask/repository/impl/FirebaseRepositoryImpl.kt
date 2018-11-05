package net.formula97.stacktask.repository.impl

import android.util.Log
import com.google.firebase.database.*
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.GlobalScope
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.runBlocking
import net.formula97.stacktask.kind.TaskItem
import net.formula97.stacktask.logic.FirebaseLogic
import net.formula97.stacktask.misc.AppConstants
import net.formula97.stacktask.repository.FirebaseRepository
import net.formula97.stacktask.repository.FutureSnapshot

class FirebaseRepositoryImpl: FirebaseRepository {

    private val database: DatabaseReference

    private val referenceTag: String = "StackTask"

    init {
//        FirebaseDatabase.getInstance().setPersistenceEnabled(true)
        database = FirebaseDatabase.getInstance().getReference(referenceTag)
    }

    override fun readTasksOnce(uid: String): MutableList<TaskItem> {
        Log.i(this.javaClass.simpleName, "called oreadTasksOnce")

        val deferred: Deferred<MutableList<TaskItem>> = GlobalScope.async(block = {

            val dbRef = database.child(uid)

            val futureSnapshot = FutureSnapshot(dbRef)

            Log.d(this.javaClass.simpleName, "started to get DataSnapshot.")

            val dataSnapshot: DataSnapshot = futureSnapshot.get()

            val result: MutableList<TaskItem> = mutableListOf()

            for (snapshot in dataSnapshot.children) {
                val i: TaskItem? = snapshot.getValue(TaskItem::class.java)
                if (i != null) {
                    result.add(i)
                }
            }

            return@async result
        })

        return runBlocking {
            Log.d(this.javaClass.simpleName, "started coroutines in runBlocking closure.")
            deferred.await()
        }
    }

    override fun addTask(uid: String, taskItem: TaskItem): String {
        val dbRef = database.child(uid)
        val key = dbRef.push().key

        taskItem.taskId = key!!
        val taskMap: HashMap<String, Any?> = kindToMap(taskItem)

        val childUpdateMap: HashMap<String, Any?> = HashMap()
        childUpdateMap[key] = taskMap

        dbRef.updateChildren(childUpdateMap)
                .addOnSuccessListener {
                    Log.d("", "追加成功")
                }
                .addOnFailureListener { exception ->
                    Log.w("", exception)
                }

        return key
    }

    override fun updateTask(uid: String, taskItem: TaskItem) {
        val taskMap: HashMap<String, Any?> = kindToMap(taskItem)

        val key = taskItem.taskId

        val childUpdateMap: HashMap<String, Any?> = HashMap()
        childUpdateMap[key] = taskMap

        val dbRef = database.child(uid)
        dbRef.updateChildren(childUpdateMap)
                .addOnSuccessListener {
                    Log.d("", "更新成功")
                }
                .addOnFailureListener { exception ->
                    Log.w("", exception)
                }
    }

    override fun addTask(uid: String, taskItem: TaskItem, callback: FirebaseLogic.OnSubmitFinishedListener): String {
        val key = database.push().key

        taskItem.taskId = key!!
        val taskMap: HashMap<String, Any?> = kindToMap(taskItem)

        val childUpdateMap: HashMap<String, Any?> = HashMap()
        childUpdateMap[key] = taskMap

        val dbRef = database.child(uid)
        dbRef.updateChildren(childUpdateMap)
                .addOnSuccessListener {
                    callback.onSuccess(AppConstants.SUBMIT_ADD)
                }
                .addOnFailureListener { exception ->
                    callback.onFailure(AppConstants.SUBMIT_ADD, exception)
                }

        return key
    }

    override fun updateTask(uid: String, taskItem: TaskItem, callback: FirebaseLogic.OnSubmitFinishedListener) {
        val taskMap: HashMap<String, Any?> = kindToMap(taskItem)

        val key = taskItem.taskId

        val childUpdateMap: HashMap<String, Any?> = HashMap()
        childUpdateMap[key] = taskMap

        val dbRef = database.child(uid)
        dbRef.updateChildren(childUpdateMap)
                .addOnSuccessListener {
                    callback.onSuccess(AppConstants.SUBMIT_UPDATE)
                }
                .addOnFailureListener { exception ->
                    callback.onFailure(AppConstants.SUBMIT_UPDATE, exception)
                }
    }

    private fun kindToMap(taskitem: TaskItem): HashMap<String, Any?> {
        val taskMap: HashMap<String, Any?> = HashMap()
        taskMap["taskId"] = taskitem.taskId
        taskMap["taskName"] = taskitem.taskName
        taskMap["dueDate"] = taskitem.dueDate
        taskMap["priority"] = taskitem.priority
        taskMap["taskDetail"] = taskitem.taskDetail
        taskMap["finished"] = taskitem.finished
        taskMap["createdAt"] = taskitem.createdAt
        taskMap["updatedAt"] = taskitem.updatedAt
        return taskMap
    }

    override fun getReference(uid: String): DatabaseReference {
        return database.child(uid)
    }
}