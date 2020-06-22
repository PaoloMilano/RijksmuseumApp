package com.magicbluepenguin.rijksmuseumapp.data

data class RijksArtObject(
    val objectNumber: String,
    val title: String? = "",
    val principalOrFirstMaker: String? = "",
    val headerImage: String? = "",
    val webImage: String? = "",
    val presentingDate: String? = "",
    val plaqueDescription: String? = "",
    val creditLine: String? = ""
)
