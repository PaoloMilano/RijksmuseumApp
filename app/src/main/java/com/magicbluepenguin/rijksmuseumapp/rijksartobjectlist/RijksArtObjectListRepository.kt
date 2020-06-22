package com.magicbluepenguin.rijksmuseumapp.rijksartobjectlist

import androidx.paging.PositionalDataSource
import com.magicbluepenguin.rijksmuseumapp.data.RijksArtObject
import com.magicbluepenguin.rijksmuseumapp.network.RijksMuseumCollectionListFailResponse
import com.magicbluepenguin.rijksmuseumapp.network.RijksMuseumCollectionListSuccessResponse
import com.magicbluepenguin.rijksmuseumapp.network.RijksMuseumCollectionsServiceWrapper
import com.magicbluepenguin.rijksmuseumapp.network.RijksMuseumErrorResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class RijksArtObjectListDataState

data class RijksArtObjectListDataSourceError(
    val rijksMuseumErrorResponse: RijksMuseumErrorResponse,
    val isInitialisationError: Boolean
) : RijksArtObjectListDataState()

data class RijksArtObjectListDataStateInitialising(val isInitialising: Boolean) : RijksArtObjectListDataState()

internal class RijksArtObjectListRepository @Inject constructor(private val rijksMuseumCollectionsServiceWrapper: RijksMuseumCollectionsServiceWrapper) {

    fun getRijksArtObjectDataSource(coroutineScope: CoroutineScope, dataStateHandler: (RijksArtObjectListDataState) -> Unit) =
        RijksArtObjectDataSource(coroutineScope, dataStateHandler, rijksMuseumCollectionsServiceWrapper)

    internal class RijksArtObjectDataSource constructor(
        private val coroutineScope: CoroutineScope,
        private val dataStateHandler: (RijksArtObjectListDataState) -> Unit,
        val rijksMuseumCollectionsServiceWrapper: RijksMuseumCollectionsServiceWrapper
    ) : PositionalDataSource<RijksArtObject>() {

        override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<RijksArtObject>) {
            coroutineScope.launch {
                rijksMuseumCollectionsServiceWrapper.listArtObjects(params.startPosition / params.loadSize, params.loadSize)
                    .let {
                        when (it) {
                            is RijksMuseumCollectionListSuccessResponse -> callback.onResult(it.artObjectList)
                            is RijksMuseumCollectionListFailResponse -> {
                                callback.onResult(emptyList())
                                dataStateHandler.invoke(RijksArtObjectListDataSourceError(it.error, false))
                            }
                        }
                    }
            }
        }

        override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<RijksArtObject>) {
            coroutineScope.launch {
                dataStateHandler.invoke(RijksArtObjectListDataStateInitialising(true))
                rijksMuseumCollectionsServiceWrapper.listArtObjects(1, params.requestedLoadSize)
                    .let {
                        when (it) {
                            is RijksMuseumCollectionListSuccessResponse -> callback.onResult(it.artObjectList, 0)
                            is RijksMuseumCollectionListFailResponse -> {
                                callback.onResult(emptyList(), 0)
                                dataStateHandler.invoke(RijksArtObjectListDataSourceError(it.error, true))
                            }
                        }
                        dataStateHandler.invoke(RijksArtObjectListDataStateInitialising(false))
                    }
            }
        }
    }
}
