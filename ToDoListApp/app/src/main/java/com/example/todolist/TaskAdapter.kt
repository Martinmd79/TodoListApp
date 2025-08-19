package com.example.todolist

import android.graphics.Paint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView

class TaskAdapter(
    private var taskList: MutableList<TaskItem>,
    private val taskViewModel: TaskViewModel // ViewModel to handle task updates and saving
) : RecyclerView.Adapter<TaskViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_cell, parent, false)
        return TaskViewHolder(view)
    }

    // Bind the task data to the views in the layout
    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = taskList[position]

        holder.taskName.text = task.name
        holder.taskDescription.text = task.description

        // Set the task completion status and update the UI accordingly
        if (task.isCompleted) {
            holder.completeButton.setImageResource(R.drawable.check_image)
            holder.taskName.paintFlags = holder.taskName.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            holder.taskDescription.paintFlags = holder.taskDescription.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        } else {
            holder.completeButton.setImageResource(R.drawable.uncheck_image)
            holder.taskName.paintFlags = holder.taskName.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            holder.taskDescription.paintFlags = holder.taskDescription.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
        }

        // Toggle task completion status when the completeButton is clicked
        holder.completeButton.setOnClickListener {
            task.isCompleted = !task.isCompleted

            // Log task completion status
            Log.d("TaskAdapter", "Task '${task.name}' marked as ${if (task.isCompleted) "completed" else "incomplete"}")


            // Update the task in the ViewModel to persist the state
            taskViewModel.updateTask(task)

            // Sort tasks to ensure correct order (only for completion)
            updateTaskOrder()

            // Refresh the RecyclerView
            notifyDataSetChanged()
        }

        // Handle info button click to edit or delete the task
        holder.infoButton.setOnClickListener {
            val activity = holder.itemView.context as AppCompatActivity
            val taskEditFragment = TaskEditFragment.newInstance(task, position)
            taskEditFragment.show(activity.supportFragmentManager, "editTaskTag")
        }
    }

    override fun getItemCount(): Int = taskList.size

    // Update the task list and refresh the RecyclerView
    fun updateTaskList(newTaskList: List<TaskItem>) {
        taskList.clear()
        taskList.addAll(newTaskList)
        notifyDataSetChanged()
    }

    // Sort the tasks based on their completion status and original order
    private fun updateTaskOrder() {
        taskList.sortWith(compareBy<TaskItem> { it.isCompleted }.thenBy { it.originalPosition })
    }
}
