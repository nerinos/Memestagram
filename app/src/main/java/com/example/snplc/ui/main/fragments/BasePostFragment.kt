package com.example.snplc.ui.main.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.RequestManager
import com.example.snplc.R
import com.example.snplc.adapters.PostAdapter
import com.example.snplc.adapters.UserAdapter
import com.example.snplc.other.EventObserver
import com.example.snplc.ui.main.dialogs.DeletePostDialog
import com.example.snplc.ui.main.dialogs.LikedByDialog
import com.example.snplc.ui.main.viewmodels.BasePostViewModel
import com.example.snplc.ui.snackbar
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

abstract class BasePostFragment(
    layoutId: Int
) : Fragment(layoutId) {

    @Inject
    lateinit var glide: RequestManager

    @Inject
    lateinit var postAdapter: PostAdapter

//    protected abstract val postProgressBar: ProgressBar

    protected abstract val basePostViewModel: BasePostViewModel

    private var curLikedIndex: Int? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        subscribeToObservers()

        postAdapter.setOnLikeClickListener { post, i ->
            curLikedIndex = i
            post.isLiked = !post.isLiked
            basePostViewModel.toggleLikeForPost(post)
        }

        postAdapter.setOnDeletePostClickListener { post ->
            DeletePostDialog().apply {
                setPositiveListener {
                    basePostViewModel.deletePost(post)
                }
            }.show(childFragmentManager, null)
        }

        postAdapter.setOnLikedByClickListener { post ->
            basePostViewModel.getUsers(post.likedBy)
        }

        postAdapter.setOnCommentsClickListener { post ->
            findNavController().navigate(
                R.id.globalActionToCommentDialog,
                Bundle().apply {
                    putString("postId", post.id)
                }
            )
        }
        postAdapter.setOnUserClickListener { authorId ->
            findNavController().navigate(
                R.id.globalActionToOthersProfileFragment,
                Bundle().apply {
                    putString("uid", authorId)
                }
            )
        }
    }

    private fun subscribeToObservers() {
        basePostViewModel.likedByUsers.observe(viewLifecycleOwner, EventObserver(
            onError = {
                snackbar(it)
            }
        ) { users ->
            val userAdapter = UserAdapter(glide)
            userAdapter.users = users
            userAdapter.setOnUserClickListener { user ->
                if (user.uid == FirebaseAuth.getInstance().uid) {
                    snackbar("Your profile")
                    findNavController().navigate(
                        SearchFragmentDirections.globalActionToProfileFragment()

                    )
                } else {
                    findNavController().navigate(
                        SearchFragmentDirections.globalActionToOthersProfileFragment(user.uid)
                    )
                }
            }
            LikedByDialog(userAdapter).show(childFragmentManager, null)
        })
        basePostViewModel.likePostStatus.observe(viewLifecycleOwner, EventObserver(
            onError = {
                curLikedIndex?.let { index ->
                    postAdapter.peek(index)?.isLiking = false
                    postAdapter.notifyItemChanged(index)
                }
                snackbar(it)

            },
            onLoading = {
                curLikedIndex?.let { index ->
                    postAdapter.peek(index)?.isLiking = true
                    postAdapter.notifyItemChanged(index)
                }
            }
        ) { isLiked ->
            curLikedIndex?.let { index ->
                val uid = FirebaseAuth.getInstance().uid!!
                postAdapter.peek(index)?.apply {
                    this.isLiked = isLiked
                    postAdapter.peek(index)?.isLiking = false
                    if (isLiked) {
                        likedBy += uid
                    } else {
                        likedBy -= uid
                    }
                }
                postAdapter.notifyItemChanged(index)
            }

        })

        // not needed with paging3
//        basePostViewModel.deletePostStatus.observe(viewLifecycleOwner, EventObserver(
//            onError = { snackbar(it) }
//        ) { deletedPost ->
//            postAdapter.posts -= deletedPost
//        })
//        basePostViewModel.posts.observe(viewLifecycleOwner, EventObserver(
//            onError = {
//                postProgressBar.isVisible = false
//                snackbar(it)
//            },
//            onLoading = {
//                postProgressBar.isVisible = true
//            }
//        ){ posts ->
//            postProgressBar.isVisible = false
//            postAdapter.posts = posts
//        })
    }
}