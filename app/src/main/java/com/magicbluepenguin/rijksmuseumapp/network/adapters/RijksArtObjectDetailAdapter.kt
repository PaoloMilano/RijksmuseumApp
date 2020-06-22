package com.magicbluepenguin.rijksmuseumapp.network.adapters

import com.magicbluepenguin.rijksmuseumapp.data.RijksArtObject
import com.squareup.moshi.FromJson

internal data class RijksArtObjectDetail(
    val artObject: Map<String, Any?>,
    val artObjectPage: Map<String, Any?>
)

internal class RijksArtObjectDetailAdapter {
    @FromJson
    fun fromJson(rijksArtObject: RijksArtObjectDetail): RijksArtObject {

        val webImageUrl =
            (rijksArtObject.artObject["webImage"] as? Map<*, *>).run {
                this?.get("url")
            }?.toString().orEmpty()

        val presentingDate =
            (rijksArtObject.artObject["dating"] as? Map<*, *>).run {
                this?.get("presentingDate")
            }?.toString().orEmpty()

        val creditLine =
            (rijksArtObject.artObject["acquisition"] as? Map<*, *>).run {
                this?.get("creditLine")
            }?.toString().orEmpty()

        return RijksArtObject(
            objectNumber = rijksArtObject.artObject["objectNumber"]?.toString().orEmpty(),
            title = rijksArtObject.artObject["title"]?.toString().orEmpty(),
            principalOrFirstMaker = rijksArtObject.artObject["principalOrFirstMaker"]?.toString().orEmpty(),
            webImage = webImageUrl,
            presentingDate = presentingDate,
            plaqueDescription = rijksArtObject.artObjectPage["plaqueDescription"]?.toString().orEmpty(),
            creditLine = creditLine
        )
    }
}
