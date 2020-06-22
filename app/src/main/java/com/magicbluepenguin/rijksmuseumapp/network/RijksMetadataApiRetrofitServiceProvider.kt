package com.magicbluepenguin.rijksmuseumapp.network

import com.magicbluepenguin.rijksmuseumapp.network.adapters.RijksArtObjectDetailAdapter
import com.magicbluepenguin.rijksmuseumapp.network.adapters.RijksArtObjectListAdapter
import com.squareup.moshi.Moshi
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

internal object RijksMuseumRetrofitServiceProvider {

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

    fun getRijksMuseumServiceWrapper(apiKey: String, language: String) =
        RijksMuseumCollectionsServiceWrapper(
            apiKey,
            language,
            retrofit.create(RijksArtObjectMetadataService::class.java)
        )
}
