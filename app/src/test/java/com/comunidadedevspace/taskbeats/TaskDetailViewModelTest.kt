package com.comunidadedevspace.taskbeats

import com.comunidadedevspace.taskbeats.data.local.Task
import com.comunidadedevspace.taskbeats.data.local.TaskDao
import com.comunidadedevspace.taskbeats.view.ActionType
import com.comunidadedevspace.taskbeats.view.TaskAction
import com.comunidadedevspace.taskbeats.view.TaskDetailViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

@OptIn(ExperimentalCoroutinesApi::class)
class TaskDetailViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val taskDao: TaskDao = mock()

    private val underTest: TaskDetailViewModel by lazy {
        TaskDetailViewModel(
            taskDao
        )
    }


/*    @Test
    fun delete_all() = runTest {
        // Given
        val taskAction = TaskAction(
            task = null,
            actionType = ActionType.DELETE_ALL.name
        )
        // When
        underTest.execute(taskAction)
        // Then
        verify(taskDao).deleteAll()
    }*/

    @Test
    fun update_task() = runTest {
        // Given
        val task = Task(
            id = 1,
            title = "title",
            description = "description"
        )
        val taskAction = TaskAction(
            task,
            actionType = ActionType.UPDATE.name
        )

        // When
        underTest.execute(taskAction)
        // Then
        verify(taskDao).update(task)
    }

    @Test
    fun delete_task() = runTest {
        // Given
        val task = Task(
            id = 1,
            title = "title",
            description = "description"
        )
        val taskAction = TaskAction(
            task,
            actionType = ActionType.DELETE.name
        )

        // When
        underTest.execute(taskAction)
        // Then
        verify(taskDao).deleteById(task.id)
    }

    @Test
    fun create_task() = runTest {
        // Given
        val task = Task(
            id = 1,
            title = "title",
            description = "description"
        )
        val taskAction = TaskAction(
            task,
            actionType = ActionType.CREATE.name
        )

        // When
        underTest.execute(taskAction)
        // Then
        verify(taskDao).insert(task)
    }

}