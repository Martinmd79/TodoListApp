package com.example.todolist

import java.io.Serializable
import java.util.UUID

class TaskItem(
    val id: String = UUID.randomUUID().toString(),  // Automatically generate a random unique ID
    var name: String,
    var description: String,
    var isCompleted: Boolean = false,  // Track if the task is completed
    var originalPosition: Int = 0      // New field to store original task position
) : Serializable
