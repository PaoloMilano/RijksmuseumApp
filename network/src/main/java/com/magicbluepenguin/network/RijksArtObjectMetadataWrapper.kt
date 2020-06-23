package com.magicbluepenguin.network

import com.magicbluepenguin.network.data.RijksArtObject
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

interface RijksMuseumCollectionsServiceWrapper {
    suspend fun listArtObjects(page: Int, pageSize: Int): RijksMuseumCollectionListResponse

    suspend fun getArtObject(objectNumber: String): RijksMuseumCollectionObjectDetailResponse
}

internal class RijksMuseumCollectionsRetrofitServiceWrapper(
    private val apiKey: String,
    private val language: String,
    private val rijksArtObjectMetadataService: RijksArtObjectMetadataService
) : RijksMuseumCollectionsServiceWrapper {
    override suspend fun listArtObjects(page: Int, pageSize: Int) = try {
        RijksMuseumCollectionListSuccessResponse(
            rijksArtObjectMetadataService.listArtObjects(
                language,
                apiKey,
                page,
                pageSize
            )
        )
    } catch (e: Exception) {
        RijksMuseumCollectionListFailResponse(e.toRijksMuseumErrorResponse())
    }

    override suspend fun getArtObject(objectNumber: String) = try {
        RijksMuseumCollectionObjectDetailSuccessResponse(
            rijksArtObjectMetadataService.getArtObject(
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