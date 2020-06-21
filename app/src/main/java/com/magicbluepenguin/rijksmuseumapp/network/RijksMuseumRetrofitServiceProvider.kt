package com.magicbluepenguin.rijksmuseumapp.network

import com.magicbluepenguin.rijksmuseumapp.data.RijksArtObject
import com.squareup.moshi.FromJson
import com.squareup.moshi.Moshi
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

private data class RijksArtObjectList(
    val artObjects: List<Map<String, Any>>
)

private data class RijksArtObjectDetail(
    val artObject: Map<String, Any>,
    val artObjectPage: Map<String, Any>
)

internal object RijksMuseumRetrofitServiceProvider {

    private val retrofit by lazy {
        class RijksArtObjectListAdapter {
            @FromJson
            fun fromJson(rijksObjectCollection: RijksArtObjectList): List<RijksArtObject> {
                return rijksObjectCollection.artObjects.map { artObjectMap ->

                    val headerImageUrl =
                        (artObjectMap["headerImage"] as? Map<String, Any>)?.run {
                            this["url"].toString()
                        }

                    RijksArtObject(
                        objectNumber = artObjectMap["objectNumber"].toString(),
                        title = artObjectMap["title"].toString(),
                        principalOrFirstMaker = artObjectMap["principalOrFirstMaker"].toString(),
                        headerImage = headerImageUrl
                    )
                }
            }
        }

        class RijksArtObjectAdapter {
            @FromJson
            fun fromJson(rijksArtObject: RijksArtObjectDetail): RijksArtObject {

                val webImageUrl =
                    (rijksArtObject.artObject["webImage"] as? Map<String, Any>)?.run {
                        this["url"].toString()
                    }
                val presentingDate =
                    (rijksArtObject.artObject["dating"] as? Map<String, Any>)?.run {
                        this["presentingDate"].toString()
                    }

                val creditLine =
                    (rijksArtObject.artObject["acquisition"] as? Map<String, Any>)?.run {
                        this["creditLine"].toString()
                    }

                return RijksArtObject(
                    objectNumber = rijksArtObject.artObject["objectNumber"].toString(),
                    title = rijksArtObject.artObject["title"].toString(),
                    principalOrFirstMaker = rijksArtObject.artObject["principalOrFirstMaker"].toString(),
                    webImage = webImageUrl,
                    presentingDate = presentingDate,
                    plaqueDescription = rijksArtObject.artObjectPage["plaqueDescription"].toString(),
                    creditLine = creditLine
                )
            }
        }

        val moshi = Moshi.Builder()
            .add(RijksArtObjectListAdapter())
            .add(RijksArtObjectAdapter())
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
            retrofit.create(RijksMuseumCollectionsService::class.java)
        )
}
