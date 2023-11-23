package com.comunidadedevspace.taskbeats

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.room.Room
import com.comunidadedevspace.taskbeats.databinding.ActivityTaskListBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    companion object {
        const val TASK_ACTION_RESULT = "TASK_ACTION_RESULT"
    }

    private lateinit var ctnContent: LinearLayout
    private val adapter: TaskListAdapter = TaskListAdapter(::onListItemClicked)

    private val dataBase by lazy {
        Room.databaseBuilder(
            applicationContext,
            AppDataBase::class.java, "taskbeats-database"
        ).build()
    }

    private val dao by lazy {
        dataBase.taskDao()
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
            val task: Task = taskAction.task

            when (taskAction.actionType) {
                ActionType.DELETE.name -> deleteById(task.id)
                ActionType.CREATE.name -> insertIntoDataBase(task)
                ActionType.UPEDATE.name -> updateIntoDataBase(task)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTaskListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayShowTitleEnabled(false)

        listFromDataBase()
        ctnContent = binding.ctnContent

        binding.recylerTaskList.adapter = adapter

        val fab = binding.fabAdd
        fab.setOnClickListener {
            openTaskListDetail()
        }
    }

    private fun insertIntoDataBase(task: Task) {
        CoroutineScope(IO).launch {
            dao.insert(task)
            listFromDataBase()
        }
    }

    private fun updateIntoDataBase(task: Task) {
        CoroutineScope(IO).launch {
            dao.update(task)
            listFromDataBase()
        }
    }

    private fun deleteAll() {
        CoroutineScope(IO).launch {
            dao.deleteAll()
            listFromDataBase()
        }
    }

    private fun deleteById(id: Int) {
        CoroutineScope(IO).launch {
            dao.deleteById(id)
            listFromDataBase()
        }
    }

    private fun listFromDataBase() {
        CoroutineScope(IO).launch {
            val myDataBaseList: List<Task> = dao.getAll()
            adapter.submitList(myDataBaseList)
        }
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
