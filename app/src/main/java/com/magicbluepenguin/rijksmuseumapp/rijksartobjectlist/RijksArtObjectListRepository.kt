package com.magicbluepenguin.rijksmuseumapp.rijksartobjectlist

import androidx.paging.PositionalDataSource
import com.magicbluepenguin.network.RijksMuseumCollectionListFailResponse
import com.magicbluepenguin.network.RijksMuseumCollectionListSuccessResponse
import com.magicbluepenguin.network.RijksMuseumCollectionsServiceWrapper
import com.magicbluepenguin.network.RijksMuseumErrorResponse
import com.magicbluepenguin.network.data.RijksArtObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

internal sealed class RijksArtObjectListDataState

internal data class RijksArtObjectListDataSourceError(
    val rijksMuseumErrorResponse: RijksMuseumErrorResponse,
    val isInitialisationError: Boolean
) : RijksArtObjectListDataState()

internal data class RijksArtObjectListDataStateInitialising(val isInitialising: Boolean) : RijksArtObjectListDataState()

internal class RijksArtObjectListRepository @Inject constructor(private val rijksMuseumCollectionsServiceWrapper: RijksMuseumCollectionsServiceWrapper) {

    fun getRijksArtObjectDataSource(coroutineScope: CoroutineScope, dataStateHandler: (RijksArtObjectListDataState) -> Unit) =
        RijksArtObjectDataSource(coroutineScope, dataStateHandler, rijksMuseumCollectionsServiceWrapper)

    internal class RijksArtObjectDataSource constructor(
        private val coroutineScope: CoroutineScope,
        private val dataStateHandler: (RijksArtObjectListDataState) -> Unit,
        val rijksMuseumCollectionsServiceWrapper: RijksMuseumCollectionsServiceWrapper
    ) : PositionalDataSource<RijksArtObject>() {

        override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<RijksArtObject>) {
            coroutineScope.launch(Dispatchers.IO) {
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
            coroutineScope.launch(Dispatchers.IO) {
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
