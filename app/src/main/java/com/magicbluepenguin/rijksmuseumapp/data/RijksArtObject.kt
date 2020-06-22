package com.magicbluepenguin.rijksmuseumapp.data

data class RijksArtObject(
    val objectNumber: String,
    val title: String? = null,
    val principalOrFirstMaker: String? = null,
    val headerImage: String? = null,
    val webImage: String? = null,
    val presentingDate: String? = null,
    val plaqueDescription: String? = null,
    val creditLine: String? = null,
    val webLink: String? = null
)
