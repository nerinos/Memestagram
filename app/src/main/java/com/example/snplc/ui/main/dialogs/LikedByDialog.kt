package com.example.snplc.ui.main.dialogs

import android.app.Dialog
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.snplc.R
import com.example.snplc.adapters.UserAdapter
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class LikedByDialog(
    private val userAdapter: UserAdapter
) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val rvLikedBy = RecyclerView(requireContext()).apply {
            adapter = userAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        return MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.liked_by_dialog_title)
            .setView(rvLikedBy)
            .create()
        return super.onCreateDialog(savedInstanceState)
    }
}