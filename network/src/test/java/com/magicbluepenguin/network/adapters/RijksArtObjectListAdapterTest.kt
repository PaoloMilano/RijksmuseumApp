package com.magicbluepenguin.network.adapters

import junit.framework.Assert.assertEquals
import org.junit.Test

class RijksArtObjectListAdapterTest {
    @Test
    fun `test detailed object parsing`() {
        val expected = com.magicbluepenguin.network.data.RijksArtObject(
            objectNumber = "123",
            title = "Title",
            principalOrFirstMaker = "Maker",
            headerImage = "https://header_image",
            webLink = "https://web_link"
        )

        val artObjectMap = mapOf(
            "objectNumber" to expected.objectNumber,
            "title" to expected.title,
            "principalOrFirstMaker" to expected.principalOrFirstMaker,
            "headerImage" to mapOf("url" to expected.headerImage),
            "links" to mapOf("web" to expected.webLink)
        )

        val generated = RijksArtObjectListAdapter()
            .fromJson(RijksArtObjectListItem(listOf(artObjectMap))).first()
        assertEquals(expected, generated)
    }

    @Test
    fun `test that optional values are turned to empty strings when null`() {
        val expected = com.magicbluepenguin.network.data.RijksArtObject(
            "123",
            title = "",
            principalOrFirstMaker = "",
            headerImage = "",
            webLink = ""
        )

        val artObjectMap = mapOf(
            "objectNumber" to "123",
            "title" to null,
            "principalOrFirstMaker" to null,
            "headerImage" to mapOf("url" to null)
        )

        val generated = RijksArtObjectListAdapter()
            .fromJson(RijksArtObjectListItem(listOf(artObjectMap))).first()
        assertEquals(expected, generated)
    }
}
