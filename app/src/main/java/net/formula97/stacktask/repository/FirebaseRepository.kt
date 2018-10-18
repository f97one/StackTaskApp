package net.formula97.stacktask.repository

import com.google.firebase.database.DatabaseReference
import net.formula97.stacktask.kind.TaskItem
import net.formula97.stacktask.logic.FirebaseLogic

interface FirebaseRepository {
    fun readTasksOnce(uid: String): MutableList<TaskItem>
    fun addTask(taskItem: TaskItem): String
    fun addTask(taskItem: TaskItem, callback: FirebaseLogic.OnSubmitFinishedListener): String
    fun updateTask(taskItem: TaskItem)
    fun updateTask(taskItem: TaskItem, callback: FirebaseLogic.OnSubmitFinishedListener)
    fun getReference(): DatabaseReference
}