package com.magicbluepenguin.network

import com.magicbluepenguin.network.data.RijksArtObject
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

internal interface RijksArtObjectMetadataService {

    @GET("{language}/collection")
    suspend fun listArtObjects(
        @Path("language") language: String,
        @Query("key") apiKey: String,
        @Query("p") page: Int,
        @Query("ps") pageSize: Int
    ): List<RijksArtObject>

    @GET("{language}/collection/{objectNumber}")
    suspend fun getArtObject(
        @Path("language") language: String,
        @Path("objectNumber") objectNumber: String,
        @Query("key") apiKey: String
    ): RijksArtObject
}
