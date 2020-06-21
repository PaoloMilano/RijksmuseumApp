package com.magicbluepenguin.rijksmuseumapp.network

internal class RijksMuseumCollectionsServiceWrapper(
    private val apiKey: String,
    private val language: String,
    private val rijksMuseumCollectionsService: RijksMuseumCollectionsService
) {
    suspend fun listArtObjects(offset: Int, limit: Int) =
        rijksMuseumCollectionsService.listArtObjects(language, apiKey, offset, limit)

    suspend fun getArtObject(objectNumber: String) =
        rijksMuseumCollectionsService.getArtObject(language, objectNumber, apiKey)
}
