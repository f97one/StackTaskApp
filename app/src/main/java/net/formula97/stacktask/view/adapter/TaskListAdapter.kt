package net.formula97.stacktask.view.adapter

import android.graphics.Paint
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import net.formula97.stacktask.R
import net.formula97.stacktask.kind.TaskItem
import net.formula97.stacktask.misc.AppConstants
import net.formula97.stacktask.view.holder.TaskListViewHolder
import java.text.SimpleDateFormat
import java.util.*
import kotlin.Comparator
import kotlin.concurrent.withLock

class TaskListAdapter(private var taskList: MutableList<TaskItem>): RecyclerView.Adapter<TaskListViewHolder>(), RecyclerViewAdapterOperators<TaskItem> {
    override fun getItem(position: Int): TaskItem {
        return taskList[position]
    }

    override fun remove(item: TaskItem) {
        reentrantLock.withLock {
            val position = taskList.indexOf(item)
            taskList.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    override fun sort(comparator: Comparator<TaskItem>) {
        reentrantLock.withLock {
            Collections.sort(taskList, comparator)
            notifyDataSetChanged()
        }
    }

    override fun reverse() {
        reentrantLock.withLock {
            taskList.reverse()
            notifyDataSetChanged()
        }
    }

    override fun clear(refresh: Boolean) {
        reentrantLock.withLock {
            taskList.clear()
            if (refresh) {
                notifyDataSetChanged()
            }
        }
    }

    override fun addAll(dataset: MutableList<TaskItem>) {
        reentrantLock.withLock {
            taskList.addAll(dataset)
            notifyDataSetChanged()
        }
    }

    interface OnItemClickListener: EventListener {
        fun onItemClick(view: View, position: Int, item: TaskItem)
    }

    interface OnItemCheckedChangeListener: EventListener {
        fun onItemCheckedChange(view: View, position: Int, checked: Boolean, item: TaskItem)
    }

    private lateinit var onItemClickLister: OnItemClickListener

    private lateinit var onItemCheckedChangeListener: OnItemCheckedChangeListener

    override fun onCreateViewHolder(parent: ViewGroup, viewtype: Int): TaskListViewHolder {
        val v: View = LayoutInflater.from(parent.context).inflate(R.layout.task_item, parent, false)
        return TaskListViewHolder(v)
    }

    override fun getItemCount(): Int {
        return taskList.size
    }

    override fun onBindViewHolder(holder: TaskListViewHolder, position: Int) {
        val item = taskList[position]

        holder.itemTaskName.text = item.taskName
        holder.itemCompletedCheck.isChecked = item.finished

        val sdf = SimpleDateFormat(AppConstants.APP_STANDARD_DATETIME_FORMAT, Locale.getDefault())
        val cal = Calendar.getInstance()
        cal.timeInMillis = item.dueDate
        holder.itemDueDate.text = sdf.format(cal.time)

        val textPaint = holder.itemTaskName.paint
        if (item.finished) {
            // タスク名に取り消し線を描画
            textPaint.flags = (holder.itemTaskName.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG)
        } else {
            // 取り消し線のフラグだけを下げるため、「取り消し線フラグの否定値」とandをとる
            val masked = Paint.STRIKE_THRU_TEXT_FLAG.inv()
            textPaint.flags = (holder.itemTaskName.paintFlags and masked)
        }
        textPaint.isAntiAlias = true

        holder.itemPriority.rating = item.priority.toFloat()

        // ItemClickListener
        holder.itemTaskName.setOnClickListener { view ->
            onItemClickLister.onItemClick(view, position, item)
        }
        holder.itemPriority.setOnClickListener { view ->
            onItemClickLister.onItemClick(view, position, item)
        }
        holder.itemDueDate.setOnClickListener { view ->
            onItemClickLister.onItemClick(view, position, item)
        }
        holder.taskItemLayout.setOnClickListener { v ->
            onItemClickLister.onItemClick(v, position, item)
        }

        holder.itemCompletedCheck.setOnCheckedChangeListener { buttonView, isChecked ->
            onItemCheckedChangeListener.onItemCheckedChange(buttonView, position, isChecked, item)
        }
    }

    fun setOnItemClickLister(callback: OnItemClickListener) {
        onItemClickLister = callback
    }

    fun setOnItemCheckedChangeListener(callback: OnItemCheckedChangeListener) {
        onItemCheckedChangeListener = callback
    }

    override fun addItem(item: TaskItem) {
        reentrantLock.withLock {
            taskList.add(item)
            notifyItemInserted(itemCount)
        }
    }

    fun addItem(indexOf: Int, taskItem: TaskItem) {
        reentrantLock.withLock {
            taskList.add(indexOf, taskItem)
            notifyItemInserted(indexOf)
        }
    }

    fun replaceItem(index: Int, item: TaskItem) {
        reentrantLock.withLock {
            taskList[index] = item
            notifyItemChanged(index)
        }
    }

    fun replaceItems(items: MutableList<TaskItem>) {
        clear(false)
        reentrantLock.withLock {
            this.taskList = items
            notifyDataSetChanged()
        }
    }

    override fun indexOf(item: TaskItem): Int {
        return taskList.indexOf(item)
    }
}