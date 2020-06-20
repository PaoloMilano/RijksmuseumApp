package com.magicbluepenguin.rijksmuseumapp.data

data class RijksArtObject(
    val objectNumber: String,
    val title: String,
    val principalOrFirstMaker: String,
    val hasImage: Boolean,
    val headerImage: String?
)