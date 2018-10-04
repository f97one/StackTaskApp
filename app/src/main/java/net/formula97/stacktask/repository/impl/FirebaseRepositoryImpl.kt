package net.formula97.stacktask.repository.impl

import com.google.firebase.auth.FirebaseUser
import net.formula97.stacktask.kind.TaskItem
import net.formula97.stacktask.repository.FirebaseRepository

class FirebaseRepositoryImpl: FirebaseRepository {
    override fun getCurrentUser(): FirebaseUser {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun signInWithGoogle() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun readTasks(uid: String): List<TaskItem> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun addTask(taskItem: TaskItem) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateTask(taskitem: TaskItem) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}