package net.formula97.stacktask.repository

import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import net.formula97.stacktask.kind.TaskItem

interface FirebaseRepository {
    fun readTasksOnce(uid: String): MutableList<TaskItem>
    fun addTask(taskItem: TaskItem)
    fun updateTask(taskitem: TaskItem)
    fun getReference(): DatabaseReference
}