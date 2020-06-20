package com.magicbluepenguin.rijksmuseumapp.cache

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.magicbluepenguin.rijksmuseumapp.data.RijksArtObject
import com.magicbluepenguin.testapplication.data.cache.AppDatabase
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
internal class RijksCollectionDaoTest {

    private lateinit var database: AppDatabase
    private lateinit var rijksCollectionsDao: RijksCollectionsDao

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(
            context, AppDatabase::class.java
        ).build()
        rijksCollectionsDao = database.rijksCollectionsDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun `test art object insertion and retrieval`() = runBlocking {
        val artObjects = listOf(
            RijksArtObject("ABC", "title1", "maker1", true, "https//:123"),
            RijksArtObject("DEF", "title2", "maker2", true, "https//:456"),
            RijksArtObject("HIJ", "title3", "maker3", true, "https//:789"),
            RijksArtObject("KLM", "title4", "maker4", true, "https//:101")
        )
        rijksCollectionsDao.insertRijksArtObjects(artObjects)
        assertEquals(artObjects, rijksCollectionsDao.getRijksArtObjects())
    }
}