package com.sample.roomandpaginlibrary

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.DataSource
import androidx.paging.ItemKeyedDataSource
import androidx.paging.PagedList
import androidx.paging.toLiveData
import com.sample.roomandpaginlibrary.model.*
import com.sample.roomandpaginlibrary.net.LingvoItem
import com.sample.roomandpaginlibrary.net.LingvoLive
import com.sample.roomandpaginlibrary.net.LingvoSearchResult
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.channels.consume
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.channels.first
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.first
import okhttp3.OkHttpClient
import java.lang.Error

@ExperimentalCoroutinesApi
@FlowPreview
class FirstViewModel(app: Application): AndroidViewModel(app) {
    var dbManager: DbManager = app.db
    val searchRepository = SearchRepository(viewModelScope)

    val words = dbManager.getWordsWithTranslations().toLiveData(pageSize = 20)
    val lingvoItems = SearchDataSourceFactory(searchRepository).toLiveData(pageSize = 20)

    init {
    }

    @FlowPreview
    @ExperimentalCoroutinesApi
    class SearchRepository(val scope: CoroutineScope) {
        private var lingvoService = LingvoLive.createLingvoLiveService()
        val resultPages =  ConflatedBroadcastChannel<Resource<List<LingvoItem>>>(Resource.Uninitialized())

        var nextPageIndex = 0
        var canLoadNextPage = true

        fun listenAsFlow(): Flow<Resource<List<LingvoItem>>> {
            return resultPages.asFlow()
        }

        fun loadNextPageIfNeeded() {
            if (canLoadNextPage && !resultPages.value.isLoading()) {
                loadNextPage()
            }
        }

        private fun loadNextPage() = scope.launch {
            val currentList = resultPages.value.data() ?: emptyList()
            val pageToLoad = nextPageIndex

            resultPages.send(resultPages.value.toLoading())

            var searchResult: LingvoSearchResult? = null
            withContext(Dispatchers.IO) {
                try {
                    searchResult = lingvoService.search("about", 1033, 1049, 1, pageToLoad, 5)
                    searchResult!!.page = pageToLoad

                    resultPages.send(resultPages.value.toLoaded(currentList + searchResult!!.items))
                } catch (e: Exception) {
                    e.printStackTrace()
                    resultPages.send(resultPages.value.toError("Search error", true))
                }
            }

            searchResult?.let {
                canLoadNextPage = it.hasNextPage
                nextPageIndex += 1
            }
        }

        fun clear() {
            resultPages.cancel(null)
        }
    }

    class SearchDataSourceFactory(val repository: SearchRepository) : DataSource.Factory<String, LingvoItem>() {
        //val sourceLiveData = MutableLiveData<ConcertTimeDataSource>()
        var latestSource: SearchDataSource? = null

        override fun create(): SearchDataSource{
            latestSource = SearchDataSource(repository)
            //sourceLiveData.postValue(latestSource)
            return latestSource!!
        }
    }

    @ExperimentalCoroutinesApi
    @FlowPreview
    class SearchDataSource(val repository: SearchRepository) : ItemKeyedDataSource<String, LingvoItem>() {
        private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
        private val flow = repository.listenAsFlow()

        override fun loadInitial(
            params: LoadInitialParams<String>,
            callback: LoadInitialCallback<LingvoItem>
        ) {
            if (!repository.canLoadNextPage) {
                callback.onResult(emptyList())
                return
            }

            repository.loadNextPageIfNeeded()
            scope.launch {
                val result = flow.first {
                    it.isLoadedOrError()
                }
                print(result)
                if (result.isError()) {
                    callback.onError(java.lang.Exception("Load Initial Error"))
                } else {
                    callback.onResult(result.data()!!.toMutableList())
                }
            }
        }

        override fun loadAfter(
            params: LoadParams<String>,
            callback: LoadCallback<LingvoItem>
        ) {
            if (!repository.canLoadNextPage) {
                callback.onResult(emptyList())
                return
            }

            repository.loadNextPageIfNeeded()
            repository.scope.launch {
                val result = flow.first {
                    it.isLoadedOrError()
                }
                print(result)
                if (result.isError()) {
                    callback.onError(java.lang.Exception("Load After Error"))
                } else {
                    callback.onResult(result.data()!!.toMutableList())
                }
            }
        }

        override fun loadBefore(
            params: LoadParams<String>,
            callback: LoadCallback<LingvoItem>
        ) {
            // don't support
        }

        override fun getKey(item: LingvoItem): String {
            return item.id
        }
    }
}