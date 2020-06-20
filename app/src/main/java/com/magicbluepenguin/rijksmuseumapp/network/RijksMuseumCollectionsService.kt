package com.magicbluepenguin.rijksmuseumapp.network

import com.magicbluepenguin.rijksmuseumapp.data.RijksArtObject
import retrofit2.http.GET

interface RijksMuseumCollectionsService {

    @GET("nl/collection?key=0fiuZFh4")
    suspend fun listArtObjects(): List<RijksArtObject?>
}