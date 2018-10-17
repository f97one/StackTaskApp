package net.formula97.stacktask.activity

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_task_editor.*
import net.formula97.stacktask.R
import net.formula97.stacktask.fragment.DateTimePickerFragment
import net.formula97.stacktask.kind.TaskItem
import net.formula97.stacktask.kind.TaskItemBuilder
import net.formula97.stacktask.misc.AppConstants
import java.text.SimpleDateFormat
import java.util.*

class TaskEditorActivity : AbstractAppActivity() {

    private lateinit var editorTaskItem: TaskItem
    private var hideDone: Boolean = false
    private var editorCalendar: Calendar = Calendar.getInstance()
    private var tmpCalendar: Calendar = Calendar.getInstance()

    private val taskItemKey = "taskItemKey"
    private val hideDoneKey = "hideDoneKey"
    private val editorCalendarKey = "editorCalendarKey"

    companion object {
        const val EXTRA_TASK_ITEM: String = "EXTRA_TASK_ITEM"
    }

    override fun inflateLayout() {
        setContentView(R.layout.activity_task_editor)
    }

    override fun onCreateImpl(savedInstanceState: Bundle?) {
        val btnResId: Int
        if (hideDone) {
            btnResId = android.R.drawable.ic_menu_add
            editorCalendar.add(Calendar.DAY_OF_MONTH, 7)

            val dateFormat = SimpleDateFormat(AppConstants.APP_STANDARD_DATETIME_FORMAT, Locale.getDefault())
            editorTaskItem.dueDate = dateFormat.format(editorCalendar.time)

        } else {
            btnResId = android.R.drawable.ic_menu_edit
        }

        submit_task_btn.setImageResource(btnResId)

        // 入力文字数の反映
        val textWatcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if ( s != null) {
                    val len = s.toString().length

                    current_length.text = len.toString()

                    if (len > 180) {
                        current_length.setTextColor(resources.getColor(android.R.color.holo_red_light))
                    } else {
                        current_length.setTextColor(resources.getColor(android.R.color.black))
                    }
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // nothing to do
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // nothing to do
            }
        }

        editor_details.addTextChangedListener(textWatcher)

        submit_task_btn.setOnClickListener { _ ->
            val taskName = editor_task_name.text.toString()
            val taskDetail = editor_details.text.toString()

            // 文字数検証
            if (taskName.length > 32) {
                Toast.makeText(applicationContext, R.string.exceed_32_chars, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (taskDetail.length > 200) {
                Toast.makeText(applicationContext, R.string.exceed_200_chars, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (preferenceLogic.isShowConfirmDialog()) {
                // TODO 確認ダイアログを出す処理を書く
            } else {
                collectFromView()

                editorTaskItem.updatedAt = Date().time

                if (hideDone) {
                    editorTaskItem.createdAt = Date().time
                    firebaseLogic.addTask(editorTaskItem)
                } else {
                    firebaseLogic.updateTask(editorTaskItem)
                }
            }

        }

        editor_due_date.setOnClickListener { _ ->
            val simpleDateFormat = SimpleDateFormat(AppConstants.APP_STANDARD_DATETIME_FORMAT, Locale.getDefault())
            val date = simpleDateFormat.parse(editorTaskItem.dueDate)
            val cal = Calendar.getInstance()
            cal.timeInMillis = date.time

            val dialog: DateTimePickerFragment = DateTimePickerFragment.create(cal)
            val callback = object : DateTimePickerFragment.OnDateTimeSetListener {
                override fun onDateTimeSet(calendar: Calendar) {
                    editorCalendar = calendar
                    editorTaskItem.dueDate = simpleDateFormat.format(calendar.time)
                }
            }
            dialog.setOnDateTimeSetListener(callback)

            dialog.show(supportFragmentManager, DateTimePickerFragment.FRAGMENT_TAG)
        }
    }

    override fun onResumeImpl() {
        editor_done.visibility = if (hideDone) {
            View.GONE
        } else {
            View.VISIBLE
        }

        editor_task_name.setText(editorTaskItem.taskName)
        editor_due_date.text = editorTaskItem.dueDate
        editor_priority.rating = editorTaskItem.priority.toFloat()
        editor_details.setText(editorTaskItem.taskDetail)
        editor_done.isChecked = editorTaskItem.finished
        current_length.text = editorTaskItem.taskDetail.length.toString()
    }

    override fun initToolBar() {
        super.initToolBar()

        val receivedTaskItem = intent.getSerializableExtra(EXTRA_TASK_ITEM) as TaskItem?

        if (receivedTaskItem == null) {
            supportActionBar!!.title = getString(R.string.add_task)
            editorTaskItem = TaskItemBuilder("").createAsDefault(firebaseLogic.getCurrentUser()!!)
            hideDone = true
        } else {
            supportActionBar!!.title = getString(R.string.edit_task)
            editorTaskItem = receivedTaskItem as TaskItem
            hideDone = false
        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)

        collectFromView()

        outState!!.putSerializable(taskItemKey, editorTaskItem)
        outState.putBoolean(hideDoneKey, hideDone)
        outState.putSerializable(editorCalendarKey, editorCalendar)
    }

    private fun collectFromView() {
        editorTaskItem.taskName = editor_task_name.text.toString()
        editorTaskItem.dueDate = editor_due_date.text.toString()
        editorTaskItem.priority = editor_priority.rating.toInt()
        editorTaskItem.taskDetail = editor_details.text.toString()
        editorTaskItem.finished = editor_done.isChecked
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        editorTaskItem = savedInstanceState!!.getSerializable(taskItemKey) as TaskItem
        hideDone = savedInstanceState.getBoolean(hideDoneKey)
        editorCalendar = savedInstanceState.getSerializable(editorCalendarKey) as Calendar
    }
}
