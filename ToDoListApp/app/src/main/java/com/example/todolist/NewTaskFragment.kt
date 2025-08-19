package com.example.todolist

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar

class NewTaskFragment : BottomSheetDialogFragment() {

    private lateinit var taskViewModel: TaskViewModel

    @SuppressLint()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_new_task, container, false)

        // Initialize ViewModel
        taskViewModel = ViewModelProvider(requireActivity()).get(TaskViewModel::class.java)

        // Find views by ID
        val taskNameEditText = view.findViewById<EditText>(R.id.name)
        val taskDescEditText = view.findViewById<EditText>(R.id.desc)
        val saveButton = view.findViewById<Button>(R.id.saveBtn)

        // Set save button listener
        saveButton.setOnClickListener {
            val taskName = taskNameEditText.text.toString().trim()
            val taskDesc = taskDescEditText.text.toString().trim()

            if (taskName.isBlank()) {
                // Show Snackbar if task name is empty
                Snackbar.make(view, "Task name cannot be empty", Snackbar.LENGTH_LONG).show()
            } else {
                // Proceed with task creation if task name is valid
                val newTask = TaskItem(name = taskName, description = taskDesc)
                taskViewModel.addTask(newTask) // Add the new task

                // add a snackbar message after adding a task
                Snackbar.make(requireActivity().findViewById(R.id.recyclerViewTasks),
                    "New task '${newTask.name}' added successfully",
                    Snackbar.LENGTH_SHORT).show()


                // Clear input fields after saving
                taskNameEditText.setText("")
                taskDescEditText.setText("")

                // Dismiss the dialog
                dismiss()

            }


        }

        return view
    }
}
