package net.formula97.stacktask.view.adapter

import java.util.concurrent.locks.ReentrantLock

interface RecyclerViewAdapterOperators<T> {

    val reentrantLock: ReentrantLock
        get() = ReentrantLock()

    fun getItem(position: Int): T
    fun indexOf(item: T): Int
    fun remove(item: T)
    fun addItem(item: T)
    fun sort(comparator: Comparator<T>)
    fun reverse()
    fun clear(refresh: Boolean = false)
    fun addAll(dataset: MutableList<T>)

}