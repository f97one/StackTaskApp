package net.formula97.stacktask.view.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
//import kotlinx.android.synthetic.main.drawer_list_item.view.*
import net.formula97.stacktask.R

/**
 * DrawerList用ArrayAdapter
 */
class DrawerItemAdapter(context: Context, private val layoutResId: Int, private val itemList: MutableList<String>)
    : ArrayAdapter<String>(context, layoutResId, itemList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val v: View = convertView ?: View.inflate(context, layoutResId, null)

        val drawerItem: TextView = v.findViewById(R.id.drawer_selection_item)
        drawerItem.text = itemList[position]

        return v
    }
}