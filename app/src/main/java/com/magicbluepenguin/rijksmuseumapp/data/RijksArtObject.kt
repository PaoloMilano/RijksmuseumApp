package com.magicbluepenguin.rijksmuseumapp.data

data class RijksArtObject(
    val objectNumber: String,
    val title: String,
    val principalOrFirstMaker: String,
    val headerImage: String? = null,
    val webImage: String? = null,
    val presentingDate: String? = null,
    val plaqueDescription: String? = null,
    val creditLine: String? = null
)
