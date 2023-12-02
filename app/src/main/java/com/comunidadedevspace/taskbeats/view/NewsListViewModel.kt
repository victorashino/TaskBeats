package com.comunidadedevspace.taskbeats.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.comunidadedevspace.taskbeats.data.remote.NewsDto
import com.comunidadedevspace.taskbeats.data.remote.NewsService
import com.comunidadedevspace.taskbeats.data.remote.RetrofitModule
import kotlinx.coroutines.launch

class NewsListViewModel(
    private val newsService: NewsService
) : ViewModel() {

    private val _newListLiveData = MutableLiveData<List<NewsDto>> ()
    val newListLiveData: LiveData<List<NewsDto>> = _newListLiveData

    init {
        getNewList()
    }

    private fun getNewList() {
        viewModelScope.launch {
            try {
                val topNews = newsService.fetchTopNews().data
                val allNews = newsService.fetchAllNews().data
                _newListLiveData.value = topNews + allNews
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
    }

    companion object {

        fun create(): NewsListViewModel {
            val newsService = RetrofitModule.createNewService()
            return NewsListViewModel(newsService)
        }
    }


}