package com.comunidadedevspace.taskbeats.view

import android.os.Bundle
import android.telecom.Call
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.comunidadedevspace.taskbeats.R
import com.comunidadedevspace.taskbeats.data.local.Task
import com.comunidadedevspace.taskbeats.data.remote.NewsResponse
import com.comunidadedevspace.taskbeats.data.remote.NewsService
import com.comunidadedevspace.taskbeats.data.remote.RetrofitModule
import retrofit2.Callback
import retrofit2.Response

/**
 * A simple [Fragment] subclass.
 * Use the [TaskListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TaskListFragment : Fragment() {

    private lateinit var ctnContent: LinearLayout

    private val retrofitModule = RetrofitModule

    private val adapter: TaskListAdapter by lazy {
        TaskListAdapter(::openTaskListDetail)
    }

    private val viewModel: TaskListViewModel by lazy {
        TaskListViewModel.create(requireActivity().application)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_task_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ctnContent = view.findViewById(R.id.ctn_content)

        val recyclerViewTasks: RecyclerView = view.findViewById(R.id.recyler_task_list)
        recyclerViewTasks.adapter = adapter

    }

    override fun onStart() {
        super.onStart()
        listFromDataBase()
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
        viewModel.taskListLiveData.observe(this, listObserver)
    }

    private fun openTaskListDetail(task: Task) {
        val intent = TaskDetailActivity.start(requireContext(), task)
        requireActivity().startActivity(intent)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         * @return A new instance of fragment TaskListFragment.
         */
        @JvmStatic
        fun newInstance() = TaskListFragment()
    }
}