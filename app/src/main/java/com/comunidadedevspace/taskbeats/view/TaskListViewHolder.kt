package com.comunidadedevspace.taskbeats.view

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.comunidadedevspace.taskbeats.data.local.Task
import com.comunidadedevspace.taskbeats.databinding.ItemTaskBinding

class TaskListViewHolder(
    private val bind: ItemTaskBinding,
    private val view: View
) : RecyclerView.ViewHolder(bind.root) {


    fun bind(task: Task, openTaskDetailView: (task: Task) -> Unit) {

        bind.textTaskTitle.text = task.title
        bind.textTaskDescription.text = task.description

        view.setOnClickListener {
            openTaskDetailView.invoke(task)
        }

    }

}