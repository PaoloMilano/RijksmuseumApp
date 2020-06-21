package com.magicbluepenguin.rijksmuseumapp.rijksartobjectlist.repository

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import androidx.paging.PositionalDataSource
import com.magicbluepenguin.rijksmuseumapp.data.RijksArtObject
import com.magicbluepenguin.rijksmuseumapp.network.RijksMuseumCollectionsServiceWrapper
import kotlinx.coroutines.*

internal class RijksArtObjectRepository(private val rijksMuseumCollectionsServiceWrapper: RijksMuseumCollectionsServiceWrapper) {

    private val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private inner class RijksArtObjectDataSource : PositionalDataSource<RijksArtObject>() {
        override fun loadRange(
            params: LoadRangeParams,
            callback: LoadRangeCallback<RijksArtObject>
        ) {
            coroutineScope.launch {
                callback.onResult(
                    rijksMuseumCollectionsServiceWrapper.listArtObjects(
                        params.startPosition,
                        params.loadSize
                    )
                )
            }
        }

        override fun loadInitial(
            params: LoadInitialParams,
            callback: LoadInitialCallback<RijksArtObject>
        ) {
            coroutineScope.launch {
                callback.onResult(
                    rijksMuseumCollectionsServiceWrapper.listArtObjects(
                        0,
                        params.requestedLoadSize
                    ),
                    0
                )
            }
        }
    }

    private val sourceLiveData = MutableLiveData<PositionalDataSource<RijksArtObject>>()

    val rijksObjectDataSourceFactory = object :
        DataSource.Factory<Int, RijksArtObject>() {
        override fun create(): DataSource<Int, RijksArtObject> {
            val dataSource = RijksArtObjectDataSource()
            sourceLiveData.postValue(dataSource)
            return dataSource
        }
    }

    fun loadData() {
        sourceLiveData.value?.invalidate()
    }

    fun onCleared() {
        coroutineScope.cancel()
    }
}
