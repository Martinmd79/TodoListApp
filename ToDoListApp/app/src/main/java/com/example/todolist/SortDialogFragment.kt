package com.example.todolist

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.appcompat.app.AlertDialog

class SortDialogFragment : DialogFragment() {

    interface SortOptionListener {
        fun onSortOptionSelected(option: SortOption)
    }

    enum class SortOption {
        CREATION_DATE, ALPHABETICALLY
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val options = arrayOf("Creation Date", "Alphabetically")
        val builder = AlertDialog.Builder(requireActivity())
        builder.setTitle("Sort Tasks")
            .setItems(options) { _, which ->
                // Communicate the selected option back to the MainActivity
                val listener = activity as? SortOptionListener
                when (which) {
                    0 -> listener?.onSortOptionSelected(SortOption.CREATION_DATE)
                    1 -> listener?.onSortOptionSelected(SortOption.ALPHABETICALLY)
                }
            }
        return builder.create()
    }
}
