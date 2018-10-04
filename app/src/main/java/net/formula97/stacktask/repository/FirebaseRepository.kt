package net.formula97.stacktask.repository

import com.google.firebase.auth.FirebaseUser
import net.formula97.stacktask.kind.TaskItem

interface FirebaseRepository {
    fun getCurrentUser(): FirebaseUser
    fun signInWithGoogle()
    fun readTasks(uid: String): List<TaskItem>
    fun addTask(taskItem: TaskItem)
    fun updateTask(taskitem: TaskItem)
}