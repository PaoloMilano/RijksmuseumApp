package com.magicbluepenguin.rijksmuseumapp.network

import com.magicbluepenguin.rijksmuseumapp.data.RijksArtObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException

sealed class RijksMuseumCollectionListResponse
data class RijksMuseumCollectionListSuccessResponse(val artObjectList: List<RijksArtObject>) : RijksMuseumCollectionListResponse()
data class RijksMuseumCollectionListFailResponse(val error: RijksMuseumErrorResponse) : RijksMuseumCollectionListResponse()

sealed class RijksMuseumCollectionObjectDetailResponse
data class RijksMuseumCollectionObjectDetailSuccessResponse(val artObject: RijksArtObject) : RijksMuseumCollectionObjectDetailResponse()
data class RijksMuseumCollectionObjectDetailFailResponse(val error: RijksMuseumErrorResponse) : RijksMuseumCollectionObjectDetailResponse()

sealed class RijksMuseumErrorResponse
object RijksMuseumNetworkErrorResponse : RijksMuseumErrorResponse()
object RijksMuseumServerErrorResponse : RijksMuseumErrorResponse()

internal class RijksMuseumCollectionsServiceWrapper(
    private val apiKey: String,
    private val language: String,
    private val rijksMuseumCollectionsService: RijksMuseumCollectionsService
) {
    suspend fun listArtObjects(page: Int, pageSize: Int): RijksMuseumCollectionListResponse = withContext(Dispatchers.IO) {
        try {
            RijksMuseumCollectionListSuccessResponse(
                rijksMuseumCollectionsService.listArtObjects(
                    language,
                    apiKey,
                    page,
                    pageSize
                )
            )
        } catch (e: Exception) {
            RijksMuseumCollectionListFailResponse(e.toRijksMuseumErrorResponse())
        }
    }

    suspend fun getArtObject(objectNumber: String): RijksMuseumCollectionObjectDetailResponse = withContext(Dispatchers.IO) {
        try {
            RijksMuseumCollectionObjectDetailSuccessResponse(
                rijksMuseumCollectionsService.getArtObject(
                    language,
                    objectNumber,
                    apiKey
                )
            )
        } catch (e: Exception) {
            RijksMuseumCollectionObjectDetailFailResponse(e.toRijksMuseumErrorResponse())
        }
    }

    private fun Exception.toRijksMuseumErrorResponse() = when (this) {
        is HttpException -> RijksMuseumServerErrorResponse
        else -> RijksMuseumNetworkErrorResponse
    }
}
