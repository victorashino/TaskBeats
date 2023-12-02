package com.comunidadedevspace.taskbeats.view

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import com.comunidadedevspace.taskbeats.R
import com.comunidadedevspace.taskbeats.data.local.Task
import com.comunidadedevspace.taskbeats.databinding.ActivityTaskDetailBinding
import com.google.android.material.snackbar.Snackbar

class TaskDetailActivity : AppCompatActivity() {

    companion object {
        private const val TASK_DETAIL_EXTRA = "task.extra.detail"

        fun start(context: Context, task: Task?): Intent {
            val intent = Intent(context, TaskDetailActivity::class.java)
                .apply {
                    putExtra(TASK_DETAIL_EXTRA, task)
                }
            return intent
        }
    }

    private val viewModel: TaskDetailViewModel by viewModels {
        TaskDetailViewModel.getVMFactory(application)
    }

    private var task: Task? = null
    private lateinit var binding: ActivityTaskDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTaskDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayShowTitleEnabled(false)

        task = intent.getSerializableExtra(TASK_DETAIL_EXTRA) as Task?

        val editTitle = binding.editTextTitle
        val editDescription = binding.editTaskDescription
        val btnDone = binding.btnDone

        if (task != null) {
            editTitle.setText(task!!.title)
            editDescription.setText(task!!.description)
        }

        btnDone.setOnClickListener {
            val title = editTitle.text.toString()
            val desc = editDescription.text.toString()

            if (title.isNotEmpty() || desc.isNotEmpty()) {
                if (task == null) {
                    addOrUpdateTask(0, title, ActionType.CREATE, desc)
                } else {
                    addOrUpdateTask(task!!.id, title, ActionType.UPDATE, desc)
                }
            } else {
                showMessage(btnDone, "Fields are required")
            }
        }
    }

    private fun addOrUpdateTask(
        id: Int,
        title: String,
        actionType: ActionType,
        description: String
    ) {
        val task = Task(id, title, description)
        performAction(task, actionType)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_task_detail, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.delete_task -> {
                if (task != null) {
                    performAction(task!!, ActionType.DELETE)
                } else {
                    showMessage(binding.btnDone, "Item not found")
                }
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun performAction(task: Task, actionType: ActionType) {
        val taskAction = TaskAction(task, actionType.name)
        viewModel.execute(taskAction)
        finish()
    }

    private fun showMessage(view: View, message: String) {
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT)
            .setAction("Action", null)
            .show()
    }

}