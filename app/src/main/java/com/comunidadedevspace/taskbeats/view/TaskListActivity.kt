package com.comunidadedevspace.taskbeats.view

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.comunidadedevspace.taskbeats.R
import com.comunidadedevspace.taskbeats.data.Task
import com.comunidadedevspace.taskbeats.databinding.ActivityTaskListBinding
import com.google.android.material.snackbar.Snackbar

class TaskListActivity : AppCompatActivity() {

    companion object {
        const val TASK_ACTION_RESULT = "TASK_ACTION_RESULT"
    }

    private lateinit var ctnContent: LinearLayout
    private val adapter: TaskListAdapter = TaskListAdapter(::onListItemClicked)


    private val viewModel: TaskListViewModel by lazy {
        TaskListViewModel.create(application)
    }

    private lateinit var binding: ActivityTaskListBinding
    private val startForResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        if (result.resultCode == RESULT_OK) {
            val data = result.data
            val taskAction = requireNotNull(
                data?.getSerializableExtra(TASK_ACTION_RESULT) as TaskAction?
            )
            viewModel.execute(taskAction)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTaskListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayShowTitleEnabled(false)

        ctnContent = binding.ctnContent

        binding.recylerTaskList.adapter = adapter

        val fab = binding.fabAdd
        fab.setOnClickListener {
            openTaskListDetail()
        }
    }

    override fun onStart() {
        super.onStart()
        listFromDataBase()
    }

    private fun deleteAll() {
        val taskAction = TaskAction(null, ActionType.DELETE_ALL.name)
        viewModel.execute(taskAction)
    }

    private fun listFromDataBase() {
        val listObserver = Observer<List<Task>> { listTasks ->
            if (listTasks.isEmpty()) {
                ctnContent.visibility = View.VISIBLE
            } else {
                ctnContent.visibility = View.GONE
            }
            adapter.submitList(listTasks)
        }
        viewModel.taskListLiveData.observe(this@TaskListActivity, listObserver)
    }

    private fun showMessage(view: View, message: String) {
        view.setOnClickListener {
            Snackbar.make(it, message, Snackbar.LENGTH_SHORT)
                .setAction("Action", null)
                .show()
        }
    }

    private fun onListItemClicked(task: Task) {
        openTaskListDetail(task)
    }

    private fun openTaskListDetail(task: Task? = null) {
        val intent = TaskDetailActivity.start(this, task)
        startForResult.launch(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_task_list, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.delete_all_task -> {
                deleteAll()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}
