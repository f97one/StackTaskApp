package net.formula97.stacktask.view.adapter

import android.graphics.Paint
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import net.formula97.stacktask.R
import net.formula97.stacktask.kind.TaskItem
import net.formula97.stacktask.view.holder.TaskListViewHolder
import java.util.*

class TaskListAdapter(private var taskList: MutableList<TaskItem>): RecyclerView.Adapter<TaskListViewHolder>() {

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
        holder.itemDueDate.text = item.dueDate

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

    fun addItem(taskItem: TaskItem) {
        taskList.add(taskItem)
    }

    fun replaceItems(items: MutableList<TaskItem>) {
        this.taskList = items
    }
}