package com.magicbluepenguin.rijksmuseumapp.network.adapters

import com.magicbluepenguin.rijksmuseumapp.data.RijksArtObject
import com.squareup.moshi.FromJson

internal data class RijksArtObjectListItem(val artObjects: List<Map<String, Any?>>)

internal class RijksArtObjectListAdapter {
    @FromJson
    fun fromJson(rijksObjectCollection: RijksArtObjectListItem): List<RijksArtObject> {
        return rijksObjectCollection.artObjects.map { artObjectMap ->

            val headerImageUrl =
                (artObjectMap["headerImage"] as? Map<*, *>).run {
                    this?.get("url")
                }?.toString().orEmpty()

            val webUrl =
                (artObjectMap["links"] as? Map<*, *>).run {
                    this?.get("web")
                }?.toString().orEmpty()

            RijksArtObject(
                objectNumber = artObjectMap["objectNumber"].toString(),
                title = artObjectMap["title"]?.toString().orEmpty(),
                principalOrFirstMaker = artObjectMap["principalOrFirstMaker"]?.toString().orEmpty(),
                headerImage = headerImageUrl,
                webLink = webUrl
            )
        }
    }
}
