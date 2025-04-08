package com.example.task_app.model

import java.io.Serializable

data class Task(val key: String, val description: String, var isFinished: Boolean = false): Serializable