package com.example.task_app.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageButton
import android.widget.TextView
import android.graphics.Color
import com.example.task_app.R
import com.example.task_app.model.Task

class ItemAdapter(
    private val context: Context,
    private var items: List<Task>,
    private val onItemClick: (Int, Task) -> Unit,
    private val onDeleteClick: (Int) -> Unit,
    private val onFinishClick: (Int) -> Unit
) : BaseAdapter() {

    override fun getCount(): Int = items.size

    override fun getItem(position: Int): Any = items[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_list, parent, false)
        val itemText = view.findViewById<TextView>(R.id.item_text)
        val deleteButton = view.findViewById<ImageButton>(R.id.delete_button)
        val finishButton = view.findViewById<ImageButton>(R.id.finish_button)

        itemText.text = items[position].description

        itemText.setOnClickListener {
            onItemClick(position, items[position])
        }

        deleteButton.setOnClickListener {
            onDeleteClick(position)
        }

        finishButton.setOnClickListener {
            onFinishClick(position)
        }

        if ( items[position].isFinished) {
            finishButton.visibility = View.INVISIBLE

            view.setBackgroundColor(Color.GREEN)
        }

        return view
    }

    fun updateItems(newItems: List<Task>) {
        items = newItems
        notifyDataSetChanged()
    }
}