package net.formula97.stacktask.view.adapter

import android.graphics.Paint
import android.os.Handler
import android.os.Looper
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
        val handler = Handler(Looper.getMainLooper())
        handler.post {
            val position = taskList.indexOf(item)
            taskList.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    override fun sort(comparator: Comparator<TaskItem>) {
        val handler = Handler(Looper.getMainLooper())
        handler.post {
            Collections.sort(taskList, comparator)
            notifyDataSetChanged()
        }
    }

    override fun reverse() {
        val handler = Handler(Looper.getMainLooper())
        handler.post {
            taskList.reverse()
            notifyDataSetChanged()
        }
    }

    override fun clear(refresh: Boolean) {
        val handler = Handler(Looper.getMainLooper())
        handler.post {
            taskList.clear()
            if (refresh) {
                notifyDataSetChanged()
            }
        }
    }

    override fun addAll(dataset: MutableList<TaskItem>) {
        val handler = Handler(Looper.getMainLooper())
        handler.post {
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

        // チェックボックスの値変更通知は、自力で読みだした値を投げることで代用する
        holder.itemCompletedCheck.setOnClickListener { v ->
            val checked = holder.itemCompletedCheck.isChecked
            onItemCheckedChangeListener.onItemCheckedChange(v, position, checked, item)
        }
    }

    fun setOnItemClickLister(callback: OnItemClickListener) {
        onItemClickLister = callback
    }

    fun setOnItemCheckedChangeListener(callback: OnItemCheckedChangeListener) {
        onItemCheckedChangeListener = callback
    }

    override fun addItem(item: TaskItem) {
        val handler = Handler(Looper.getMainLooper())
        handler.post {
            taskList.add(item)
            notifyItemInserted(itemCount)
        }
    }

    fun addItem(indexOf: Int, taskItem: TaskItem) {
        val handler = Handler(Looper.getMainLooper())
        handler.post {
            taskList.add(indexOf, taskItem)
            notifyItemInserted(indexOf)
        }
    }

    fun replaceItem(index: Int, item: TaskItem) {
        val handler = Handler(Looper.getMainLooper())
        handler.post {
            taskList[index] = item
            notifyItemChanged(index)
        }
    }

    fun replaceItems(items: MutableList<TaskItem>) {
        clear(false)
        val handler = Handler(Looper.getMainLooper())
        handler.post {
            this.taskList = items
            notifyDataSetChanged()
        }
    }

    override fun indexOf(item: TaskItem): Int {
        return taskList.indexOf(item)
    }
}