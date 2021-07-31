package com.example.snplc.ui.main.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.snplc.data.entities.Post
import com.example.snplc.data.pagingsource.FollowPostsPagingSource
import com.example.snplc.other.Constants.PAGE_SIZE
import com.example.snplc.other.Event
import com.example.snplc.other.Resource
import com.example.snplc.repositories.MainRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel @ViewModelInject constructor(
    private val repository: MainRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Main
) : BasePostViewModel(repository, dispatcher){

//    private val _posts = MutableLiveData<Event<Resource<List<Post>>>>()
//    override val posts: LiveData<Event<Resource<List<Post>>>>
//        get() = _posts

//    init {
//        getPosts()
//    }

//    override fun getPosts(uid: String) {
//        _posts.postValue(Event(Resource.Loading()))
//        viewModelScope.launch(dispatcher) {
//            val result = repository.getPostsForFollows()
//            _posts.postValue(Event(result))
//        }
//    }

    val pagingFlow = Pager(PagingConfig(PAGE_SIZE)) {
        FollowPostsPagingSource(FirebaseFirestore.getInstance())
    }.flow.cachedIn(viewModelScope)
}