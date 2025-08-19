package com.example.todolist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar

class TaskEditFragment : DialogFragment() {

    private lateinit var taskViewModel: TaskViewModel
    private lateinit var task: TaskItem
    private var position: Int = -1

    companion object {
        // Create a new instance of the fragment with task and position passed in
        fun newInstance(task: TaskItem, position: Int): TaskEditFragment {
            val fragment = TaskEditFragment()
            val args = Bundle()
            args.putSerializable("task", task)
            args.putInt("position", position)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_task_edit, container, false)

        // Get the task and position passed in
        task = arguments?.getSerializable("task") as TaskItem
        position = arguments?.getInt("position") ?: -1

        // Initialize ViewModel
        taskViewModel = ViewModelProvider(requireActivity()).get(TaskViewModel::class.java)

        // Find views by ID
        val taskNameEditText = view.findViewById<EditText>(R.id.editTaskName)
        val taskDescEditText = view.findViewById<EditText>(R.id.editTaskDescription)
        val updateButton = view.findViewById<Button>(R.id.updateTaskButton)
        val deleteButton = view.findViewById<Button>(R.id.deleteTaskButton)

        // Pre-fill with existing task data
        taskNameEditText.setText(task.name)
        taskDescEditText.setText(task.description)

        // Handle save button click
        updateButton.setOnClickListener {
            task.name = taskNameEditText.text.toString()
            task.description = taskDescEditText.text.toString()
            taskViewModel.updateTask(task) // Update task in ViewModel

            // add a snackbar message after updating a task
            Snackbar.make(requireActivity().findViewById(R.id.recyclerViewTasks),
                "The task ${task.name} is updated",
                Snackbar.LENGTH_SHORT).show()

            dismiss() // Close the dialog
        }

        // Handle delete button click
        deleteButton.setOnClickListener {
            taskViewModel.deleteTask(position) // Delete the task from ViewModel

            // add a snackbar message after updating a task
            Snackbar.make(requireActivity().findViewById(R.id.recyclerViewTasks),
                "The task ${task.name} is deleted",
                Snackbar.LENGTH_SHORT).show()

            dismiss() // Close the dialog
        }

        return view
    }
}
