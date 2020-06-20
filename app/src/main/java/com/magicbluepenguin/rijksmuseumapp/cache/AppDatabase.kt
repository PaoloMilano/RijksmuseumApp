package com.magicbluepenguin.testapplication.data.cache

import androidx.room.Database
import androidx.room.RoomDatabase
import com.magicbluepenguin.rijksmuseumapp.cache.RijksCollectionsDao
import com.magicbluepenguin.rijksmuseumapp.data.RijksArtObject

@Database(entities = arrayOf(RijksArtObject::class), version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun rijksCollectionsDao(): RijksCollectionsDao
}