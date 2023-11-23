package com.comunidadedevspace.taskbeats

import java.io.Serializable

data class TaskAction(
    val task: Task,
    val actionType: String
) : Serializable {


}
