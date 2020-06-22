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

data class RijksArtObjectListDataSourceError(
    val rijksMuseumErrorResponse: RijksMuseumErrorResponse,
    val isInitialisationError: Boolean
)

internal class RijksArtObjectListRepository @Inject constructor(private val rijksMuseumCollectionsServiceWrapper: RijksMuseumCollectionsServiceWrapper) {

    fun getRijksArtObjectDataSource(coroutineScope: CoroutineScope, errorHandler: (RijksArtObjectListDataSourceError) -> Unit) =
        RijksArtObjectDataSource(coroutineScope, errorHandler, rijksMuseumCollectionsServiceWrapper)

    internal class RijksArtObjectDataSource constructor(
        private val coroutineScope: CoroutineScope,
        private val errorHandler: (RijksArtObjectListDataSourceError) -> Unit,
        val rijksMuseumCollectionsServiceWrapper: RijksMuseumCollectionsServiceWrapper
    ) : PositionalDataSource<RijksArtObject>() {

        override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<RijksArtObject>) {
            coroutineScope.launch {
                rijksMuseumCollectionsServiceWrapper.listArtObjects(params.startPosition / params.loadSize, params.loadSize)
                    .let {
                        val g = when (it) {
                            is RijksMuseumCollectionListSuccessResponse -> callback.onResult(it.artObjectList)
                            is RijksMuseumCollectionListFailResponse -> {
                                callback.onResult(emptyList())
                                errorHandler.invoke(RijksArtObjectListDataSourceError(it.error, false))
                            }
                        }
                    }
            }
        }

        override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<RijksArtObject>) {
            coroutineScope.launch {
                rijksMuseumCollectionsServiceWrapper.listArtObjects(1, params.requestedLoadSize)
                    .let {
                        when (it) {
                            is RijksMuseumCollectionListSuccessResponse -> callback.onResult(it.artObjectList, 0)
                            is RijksMuseumCollectionListFailResponse -> {
                                callback.onResult(emptyList(), 0)
                                errorHandler.invoke(RijksArtObjectListDataSourceError(it.error, true))
                            }
                        }
                    }
            }
        }
    }
}
