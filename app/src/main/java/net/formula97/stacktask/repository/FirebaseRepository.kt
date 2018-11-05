package net.formula97.stacktask.repository

import com.google.firebase.database.DatabaseReference
import net.formula97.stacktask.kind.TaskItem
import net.formula97.stacktask.logic.FirebaseLogic

interface FirebaseRepository {
    fun readTasksOnce(uid: String): MutableList<TaskItem>
    fun addTask(uid: String, taskItem: TaskItem): String
    fun addTask(uid: String, taskItem: TaskItem, callback: FirebaseLogic.OnSubmitFinishedListener): String
    fun updateTask(uid: String, taskItem: TaskItem)
    fun updateTask(uid: String, taskItem: TaskItem, callback: FirebaseLogic.OnSubmitFinishedListener)
    fun getReference(uid: String): DatabaseReference
}