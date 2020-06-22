package com.magicbluepenguin.rijksmuseumapp.network

import com.magicbluepenguin.rijksmuseumapp.data.RijksArtObject
import retrofit2.HttpException

sealed class RijksMuseumCollectionResponse

data class RijksMuseumArtObjectListResponse(val artObjectList: List<RijksArtObject>) :
    RijksMuseumCollectionResponse()

data class RijksMuseumArtObjectDetailResponse(val artObject: RijksArtObject) :
    RijksMuseumCollectionResponse()

sealed class RijksMuseumErrorResponse : RijksMuseumCollectionResponse()
object RijksMuseumNetworkErrorResponse : RijksMuseumErrorResponse()
object RijksMuseumServerErrorResponse : RijksMuseumErrorResponse()

internal class RijksMuseumCollectionsServiceWrapper(
    private val apiKey: String,
    private val language: String,
    private val rijksMuseumCollectionsService: RijksMuseumCollectionsService
) {
    suspend fun listArtObjects(offset: Int, limit: Int): RijksMuseumCollectionResponse {
        return try {
            RijksMuseumArtObjectListResponse(
                rijksMuseumCollectionsService.listArtObjects(
                    language,
                    apiKey,
                    offset,
                    limit
                )
            )
        } catch (e: Exception) {
            e.toRijksMuseumErrorResponse()
        }
    }

    suspend fun getArtObject(objectNumber: String): RijksMuseumCollectionResponse {
        return try {
            RijksMuseumArtObjectDetailResponse(
                rijksMuseumCollectionsService.getArtObject(
                    language,
                    objectNumber,
                    apiKey
                )
            )
        } catch (e: Exception) {
            e.toRijksMuseumErrorResponse()
        }
    }

    private fun Exception.toRijksMuseumErrorResponse() = when (this) {
        is HttpException -> RijksMuseumServerErrorResponse
        else -> RijksMuseumNetworkErrorResponse
    }
}
