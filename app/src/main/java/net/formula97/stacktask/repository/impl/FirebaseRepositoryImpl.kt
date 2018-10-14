package net.formula97.stacktask.repository.impl

import android.util.Log
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.database.*
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.GlobalScope
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.runBlocking
import net.formula97.stacktask.kind.TaskItem
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
//        val result = emptyList<TaskItem>().toMutableList()
//
//        database.addListenerForSingleValueEvent(object : ValueEventListener {
//            override fun onCancelled(databaseError: DatabaseError) {
//
//            }
//
//            override fun onDataChange(dataSnapshot: DataSnapshot) {
//                for (snapshot in dataSnapshot.children) {
//
//                    val i: TaskItem? = snapshot.getValue(TaskItem::class.java)
//
//                    if (i != null) {
//                        result.add(i)
//                    }
//                }
//            }
//        })
//
//        return result

        Log.i(this.javaClass.simpleName, "called oreadTasksOnce")

        val deferred: Deferred<MutableList<TaskItem>> = GlobalScope.async(block = {

//            val query = database.equalTo(uid, "userId")
            val futureSnapshot = FutureSnapshot(database)

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

    override fun addTask(taskItem: TaskItem): String {
        val key = database.push().key

        taskItem.taskId = key!!
        val taskMap: HashMap<String, Any?> = kindToMap(taskItem)

        val childUpdateMap: HashMap<String, Any?> = HashMap()
        childUpdateMap[key] = taskMap

        database.updateChildren(childUpdateMap)
                .addOnSuccessListener {
                    Log.d("", "追加成功")
                }
                .addOnFailureListener { exception ->
                    Log.w("", exception)
                }

        return key
    }

    override fun updateTask(taskitem: TaskItem) {
        val taskMap: HashMap<String, Any?> = kindToMap(taskitem)

        val key = taskitem.taskId

        val childUpdateMap: HashMap<String, Any?> = HashMap()
        childUpdateMap[key] = taskMap

        database.updateChildren(childUpdateMap)
                .addOnSuccessListener {
                    Log.d("", "更新成功")
                }
                .addOnFailureListener { exception ->
                    Log.w("", exception)
                }
    }

    private fun kindToMap(taskitem: TaskItem): HashMap<String, Any?> {
        val taskMap: HashMap<String, Any?> = HashMap()
        taskMap["taskId"] = taskitem.taskId
        taskMap["userId"] = taskitem.userId
        taskMap["taskName"] = taskitem.taskName
        taskMap["dueDate"] = taskitem.dueDate
        taskMap["priority"] = taskitem.priority
        taskMap["taskDetail"] = taskitem.taskDetail
        taskMap["finished"] = taskitem.finished
        taskMap["createdAt"] = taskitem.createdAt
        taskMap["updatedAt"] = taskitem.updatedAt
        return taskMap
    }

    override fun getReference(): DatabaseReference {
        return database
    }
}