package com.example.snplc.ui.main.fragments

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.snplc.MainNavGraphDirections
import com.example.snplc.R
import com.example.snplc.adapters.UserAdapter
import com.example.snplc.other.Constants.SEARCH_TIME_DELAY
import com.example.snplc.other.EventObserver
import com.example.snplc.ui.main.viewmodels.SearchViewModel
import com.example.snplc.ui.snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ChatFragment() : Fragment(R.layout.fragment_chat) {

//    @Inject
//    lateinit var userAdapter: CAdapter

    private val viewModel: SearchViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        subscribeToObservers()

        var job: Job? = null
        etSearch.addTextChangedListener { editable ->
            job?.cancel()
            job = lifecycleScope.launch {
                delay(SEARCH_TIME_DELAY)
                editable?.let {
                    viewModel.searchUser(it.toString())
                }
            }
        }

//        userAdapter.setOnUserClickListener { user ->
//            findNavController().navigate(
//                OthersProfileFragmentDirections.globalActionToOthersProfileFragment(user.uid)
//            )
//        }
    }

    private fun setupRecyclerView() = rvSearchResults.apply {
        layoutManager = LinearLayoutManager(requireContext())
//        adapter = userAdapter
        itemAnimator = null
    }

    private fun subscribeToObservers() {
        viewModel.searchResults.observe(viewLifecycleOwner, EventObserver(
            onError = {
                searchProgressBar.isVisible = false
                snackbar(it)
            },
            onLoading = {
                searchProgressBar.isVisible = true
            }
        ) { users ->
            searchProgressBar.isVisible = false
//            userAdapter.users = users
        })
    }
}