package net.formula97.stacktask.repository

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import java.util.*
import java.util.concurrent.Future
import java.util.concurrent.TimeUnit

class FutureSnapshot(private val query: Query): Future<DataSnapshot>, ValueEventListener {

    private var cancelled: Boolean = false

    private var snapshot: DataSnapshot? = null

    init {
        query.addListenerForSingleValueEvent(this)
    }

    override fun onCancelled(databaseError: DatabaseError) {
        cancelled = true
    }

    override fun onDataChange(dataSnapshot: DataSnapshot) {
        Log.i("FutureSnapshot", "called onDataChanged()\n  snapshot = ${snapshot.toString()}")
        snapshot = dataSnapshot
    }

    override fun isDone(): Boolean {
        return snapshot != null
    }

    override fun get(): DataSnapshot {
        val before = Date().time
        while (!isDone && !cancelled) {
            // 100msずつ待ち合わせる(!)
            Thread.sleep(100)
        }
        val done = Date().time

        Log.d("FutureSnapshot", "elapsed in ${done - before} ms.")
        return snapshot!!
    }

    override fun get(timeout: Long, unit: TimeUnit?): DataSnapshot {
        Thread.sleep(unit!!.toMillis(timeout))
        return snapshot!!
    }

    override fun cancel(mayInterruptIfRunning: Boolean): Boolean {
        if (isDone) {
            return false
        }

        query.removeEventListener(this)
        cancelled = true
        return true
    }

    override fun isCancelled(): Boolean {
        return cancelled
    }
}