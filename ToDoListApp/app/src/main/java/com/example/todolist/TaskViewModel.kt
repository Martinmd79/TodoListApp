package com.example.todolist

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.reflect.TypeToken
import com.google.gson.Gson
import java.io.BufferedWriter
import java.io.OutputStreamWriter
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class TaskViewModel(application: Application) : AndroidViewModel(application) {
    val taskList = MutableLiveData<MutableList<TaskItem>>(mutableListOf())
    private val gson = Gson()

    // File name to store the tasks
    private val fileName = "tasks_data.json"

    // Save tasks to internal storage
    fun saveTasks() {
        GlobalScope.launch(Dispatchers.IO) {

            val tasks = taskList.value ?: mutableListOf()
            val jsonString = gson.toJson(tasks)

            try {
                // Write tasks to internal storage
                val fileOutputStream =
                    getApplication<Application>().openFileOutput(fileName, Context.MODE_PRIVATE)
                val writer = BufferedWriter(OutputStreamWriter(fileOutputStream))
                writer.write(jsonString)
                writer.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }

    // Load tasks from internal storage
    fun loadTasks() {
        GlobalScope.launch(Dispatchers.IO) {

            try {
                // Open the file using openFileInput
                val fileInputStream = getApplication<Application>().openFileInput(fileName)
                val inputStreamReader = fileInputStream.reader()
                val jsonString = inputStreamReader.readText()

                // Convert the JSON back into a list of tasks
                val type = object : com.google.firebase.crashlytics.buildtools.reloc.com.google.common.reflect.TypeToken<MutableList<TaskItem>>() {}.type
                val tasks: MutableList<TaskItem> = gson.fromJson(jsonString, type)

                // Sort tasks: incomplete tasks first, completed tasks at the bottom
                tasks.sortBy { it.isCompleted }

                // Post the loaded tasks to LiveData
                withContext(Dispatchers.Main) {
                    taskList.postValue(tasks)
                }

                inputStreamReader.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

    }



    // Add a task and save it
    fun addTask(task: TaskItem) {
        val currentList = taskList.value ?: mutableListOf()

        // Set original position based on current list size
        task.originalPosition = currentList.size

        currentList.add(task)
        taskList.value = currentList

        //log when a new task is added
        Log.d("TaskViewModel", "New Task Added: ${task.name}, ${task.description}")

        saveTasks() // Save to file after adding
    }


    // Update a task and save it
    fun updateTask(task: TaskItem) {
        val currentList = taskList.value ?: mutableListOf()
        val index = currentList.indexOfFirst { it.id == task.id }
        if (index >= 0) {
            currentList[index] = task
            taskList.value = currentList
            saveTasks() // Save to file after updating
        }
    }



    // Delete a task and save it
    fun deleteTask(position: Int) {
        val currentList = taskList.value ?: mutableListOf()
        if (position >= 0 && position < currentList.size) {
            currentList.removeAt(position)
            taskList.value = currentList
            saveTasks() // Save to file after deletion
        }
    }
}
