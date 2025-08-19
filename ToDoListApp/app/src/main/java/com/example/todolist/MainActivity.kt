package com.example.todolist

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.widget.Button
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import java.util.*

class MainActivity : AppCompatActivity(), SortDialogFragment.SortOptionListener {

    private lateinit var taskViewModel: TaskViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TaskAdapter
    private lateinit var searchEditText: TextInputEditText
    private lateinit var sortButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize ViewModel
        taskViewModel = ViewModelProvider(this).get(TaskViewModel::class.java)
        taskViewModel.loadTasks()

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recyclerViewTasks)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Initialize the adapter with an empty list (LiveData will update it)
        adapter = TaskAdapter(mutableListOf(), taskViewModel)
        recyclerView.adapter = adapter

        // Initialize the search box
        // Directly filter tasks inside onCreate
        searchEditText = findViewById(R.id.searchEditText)
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = s.toString().trim()
                adapter.updateTaskList(
                    taskViewModel.taskList.value?.filter {
                        it.name.contains(query, ignoreCase = true) ||
                                it.description.contains(query, ignoreCase = true)
                    } ?: mutableListOf()
                )
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        // Initialize the sort button
        sortButton = findViewById(R.id.sortButton)
        sortButton.setOnClickListener {
            // Show the SortDialogFragment when the sort button is clicked
            val sortDialog = SortDialogFragment()
            sortDialog.show(supportFragmentManager, "SortDialogFragment")
        }

        // Observe the task list and initially populate the RecyclerView
        taskViewModel.taskList.observe(this) { tasks ->
            adapter.updateTaskList(tasks)
        }

        // Handle the "New Task" button click to add a new task
        val newTaskBtn = findViewById<ExtendedFloatingActionButton>(R.id.newTaskBtn)
        newTaskBtn.setOnClickListener {
            NewTaskFragment().show(supportFragmentManager, "newTaskTag")
        }
    }

    // Implement the SortOptionListener interface from SortDialogFragment
    override fun onSortOptionSelected(option: SortDialogFragment.SortOption) {
        val currentTasks = taskViewModel.taskList.value ?: mutableListOf()

        when (option) {
            SortDialogFragment.SortOption.ALPHABETICALLY -> {
                // Sort tasks by name alphabetically
                currentTasks.sortBy { it.name.toLowerCase(Locale.ROOT) }
            }
            SortDialogFragment.SortOption.CREATION_DATE -> {
                // Sort tasks by creation date (assuming the ID correlates with creation order)
                currentTasks.sortBy { it.originalPosition }
            }
        }

        // Log sorted task list
        Log.d("MainActivity", "Tasks sorted alphabetically: ${currentTasks.map { it.name }}")

        // Update the adapter with the sorted list
        adapter.updateTaskList(currentTasks)
    }
}
