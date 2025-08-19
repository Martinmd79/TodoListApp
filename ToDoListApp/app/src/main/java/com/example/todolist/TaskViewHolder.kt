package com.example.todolist

import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val taskName: TextView = itemView.findViewById(R.id.taskName)
    val taskDescription: TextView = itemView.findViewById(R.id.taskDescription)
    val completeButton: ImageButton = itemView.findViewById(R.id.completeButton)
    val infoButton:ImageButton = itemView.findViewById(R.id.infoButton)
}
