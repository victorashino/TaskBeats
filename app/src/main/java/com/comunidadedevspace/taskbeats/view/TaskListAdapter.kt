package com.comunidadedevspace.taskbeats.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.comunidadedevspace.taskbeats.data.Task
import com.comunidadedevspace.taskbeats.databinding.ItemTaskBinding

class TaskListAdapter(
    private val openTaskDetailView: (task: Task) -> Unit
) : ListAdapter<Task, TaskListViewHolder>(TaskListAdapter) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskListViewHolder {
        val view = ItemTaskBinding
            .inflate(
                LayoutInflater
                    .from(parent.context), parent, false
            )
        return TaskListViewHolder(view, view.root)
    }

    override fun onBindViewHolder(holder: TaskListViewHolder, position: Int) {
        val task = getItem(position)
        holder.bind(task, openTaskDetailView)
    }

    companion object : DiffUtil.ItemCallback<Task>() {
        override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem.title == newItem.title &&
                    oldItem.description == newItem.description
        }

    }
}