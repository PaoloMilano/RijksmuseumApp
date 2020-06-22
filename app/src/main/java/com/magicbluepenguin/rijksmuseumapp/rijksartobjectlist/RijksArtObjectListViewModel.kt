package com.magicbluepenguin.rijksmuseumapp.rijksartobjectlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.magicbluepenguin.rijksmuseumapp.data.RijksArtObject
import com.magicbluepenguin.rijksmuseumapp.network.RijksMuseumArtObjectListResponse
import com.magicbluepenguin.rijksmuseumapp.network.RijksMuseumCollectionsServiceWrapper
import com.magicbluepenguin.rijksmuseumapp.network.RijksMuseumErrorResponse
import kotlinx.coroutines.launch
import javax.inject.Inject

data class RijksArtObjectListError(
    val rijksMuseumErrorResponse: RijksMuseumErrorResponse,
    val isInitialisationError: Boolean
)

internal class RijksArtObjectListViewModel @Inject constructor(private val rijksMuseumCollectionsServiceWrapper: RijksMuseumCollectionsServiceWrapper) :
    ViewModel() {

    companion object {
        private const val PAGE_SIZE = 100
        private const val MAX_LIST_SIZE = 1000
    }

    private val sourceLiveData = MutableLiveData<PositionalDataSource<RijksArtObject>>()

    val errorLiveData = MutableLiveData<RijksArtObjectListError>()
    val rijksArtObjectListLiveData: LiveData<PagedList<RijksArtObject>> =
        object :
            DataSource.Factory<Int, RijksArtObject>() {
            override fun create(): DataSource<Int, RijksArtObject> {
                val dataSource = RijksArtObjectDataSource()
                sourceLiveData.postValue(dataSource)
                return dataSource
            }
        }.toLiveData(
            Config(
                pageSize = PAGE_SIZE,
                enablePlaceholders = false,
                maxSize = MAX_LIST_SIZE
            )
        )

    init {
        loadData()
    }

    fun loadData() {
        sourceLiveData.value?.invalidate()
    }

    private inner class RijksArtObjectDataSource : PositionalDataSource<RijksArtObject>() {
        override fun loadRange(
            params: LoadRangeParams,
            callback: LoadRangeCallback<RijksArtObject>
        ) {
            viewModelScope.launch {
                rijksMuseumCollectionsServiceWrapper.listArtObjects(
                    params.startPosition / params.loadSize,
                    params.loadSize
                )
                    .let {
                        when (it) {
                            is RijksMuseumArtObjectListResponse -> callback.onResult(it.artObjectList)
                            is RijksMuseumErrorResponse -> {
                                callback.onResult(emptyList())
                                errorLiveData.postValue(RijksArtObjectListError(it, false))
                            }
                        }
                    }
            }
        }

        override fun loadInitial(
            params: LoadInitialParams,
            callback: LoadInitialCallback<RijksArtObject>
        ) {
            viewModelScope.launch {
                rijksMuseumCollectionsServiceWrapper.listArtObjects(1, params.requestedLoadSize)
                    .let {
                        when (it) {
                            is RijksMuseumArtObjectListResponse -> callback.onResult(
                                it.artObjectList,
                                0
                            )
                            is RijksMuseumErrorResponse -> {
                                callback.onResult(emptyList(), 0)
                                errorLiveData.postValue(RijksArtObjectListError(it, true))
                            }
                        }
                    }
            }
        }
    }
}
