package net.formula97.stacktask.view.holder

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.CheckBox
import android.widget.RatingBar
import android.widget.TextView
import net.formula97.stacktask.R

class TaskListViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    var itemCompletedCheck: CheckBox = itemView.findViewById(R.id.item_completed_check)
    var itemTaskName: TextView = itemView.findViewById(R.id.item_task_name)
    var itemDueDate: TextView = itemView.findViewById(R.id.item_due_date)
    var itemPriority: RatingBar = itemView.findViewById(R.id.item_priority)

}