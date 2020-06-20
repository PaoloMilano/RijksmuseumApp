package com.magicbluepenguin.rijksmuseumapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RijksArtObject(
    @PrimaryKey
    val objectNumber: String,
    val title: String,
    val principalOrFirstMaker: String,
    val hasImage: Boolean,
    val headerImage: String?
)