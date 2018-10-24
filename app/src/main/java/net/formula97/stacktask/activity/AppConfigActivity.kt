package net.formula97.stacktask.activity

import android.os.Bundle
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_app_config.*
import net.formula97.stacktask.R

class AppConfigActivity : AbstractAppActivity() {
    override fun inflateLayout() {
        setContentView(R.layout.activity_app_config)
    }

    override fun onCreateImpl(savedInstanceState: Bundle?) {

        val adapter = ArrayAdapter.createFromResource(this, R.array.task_order_menu, R.layout.spinner_item)
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
        task_order_by.adapter = adapter
    }

    override fun onResumeImpl() {

    }

}
