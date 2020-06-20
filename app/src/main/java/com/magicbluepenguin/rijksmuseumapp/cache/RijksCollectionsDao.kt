package com.magicbluepenguin.rijksmuseumapp.cache

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.magicbluepenguin.rijksmuseumapp.data.RijksArtObject

@Dao
interface RijksCollectionsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRijksArtObjects(items: List<RijksArtObject>)

    @Query("SELECT * FROM RijksArtObject")
    suspend fun getRijksArtObjects(): List<RijksArtObject>
}