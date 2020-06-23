package com.magicbluepenguin.network

import com.magicbluepenguin.network.adapters.RijksArtObjectDetailAdapter
import com.magicbluepenguin.network.adapters.RijksArtObjectListAdapter
import com.squareup.moshi.Moshi
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object RijksMuseumRetrofitServiceProvider {

    private val retrofit by lazy {
        val moshi = Moshi.Builder()
            .add(RijksArtObjectListAdapter())
            .add(RijksArtObjectDetailAdapter())
            .build()

        Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .baseUrl("https://www.rijksmuseum.nl/api/")
            .build()
    }

    fun getRijksMuseumServiceWrapper(apiKey: String, language: String): RijksMuseumCollectionsServiceWrapper {
        return RijksMuseumCollectionsRetrofitServiceWrapper(
            apiKey,
            language,
            retrofit.create(RijksArtObjectMetadataService::class.java)
        )
    }
}
