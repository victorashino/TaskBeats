package com.comunidadedevspace.taskbeats.view

import com.comunidadedevspace.taskbeats.data.local.Task
import java.io.Serializable

data class TaskAction(
    val task: Task?,
    val actionType: String
) : Serializable
