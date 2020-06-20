package com.magicbluepenguin.rijksmuseumapp.network

import com.magicbluepenguin.rijksmuseumapp.data.RijksArtObject
import com.squareup.moshi.FromJson
import com.squareup.moshi.Moshi
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

data class RijksObjectCollection(
    val artObjects: List<Map<String, Any>>
)

internal object RijksMuseumRetrofitServiceProvider {

    private val retrofit by lazy {
        class RijksArtObjectAdapter {
            @FromJson
            fun fromJson(rijksObjectCollection: RijksObjectCollection): List<RijksArtObject?> {
                return rijksObjectCollection.artObjects.map { artObjectMap ->

                    val headerImageUrl =
                        (artObjectMap["headerImage"] as? Map<String, Any>)?.run {
                            this["url"].toString()
                        }

                    RijksArtObject(
                        artObjectMap["objectNumber"].toString(),
                        artObjectMap["title"].toString(),
                        artObjectMap["principalOrFirstMaker"].toString(),
                        artObjectMap["hasImage"] as? Boolean ?: false,
                        headerImageUrl
                    )
                }
            }
        }

        val moshi = Moshi.Builder()
            .add(RijksArtObjectAdapter())
            .build()

        Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .baseUrl("https://www.rijksmuseum.nl/api/")
            .build()
    }

    val rijksMuseumDataService: RijksMuseumCollectionsService
        get() = retrofit.create(RijksMuseumCollectionsService::class.java)
}