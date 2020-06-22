package com.magicbluepenguin.rijksmuseumapp.network.adapters

import com.magicbluepenguin.rijksmuseumapp.data.RijksArtObject
import junit.framework.Assert.assertEquals
import org.junit.Test

class RijksArtObjectDetailAdapterTest {

    @Test
    fun `test detailed object parsing`() {

        val expected = RijksArtObject(
            "123",
            "Title",
            "Maker",
            "",
            "url",
            "Presenting Date",
            "Description",
            "creditLine"
        )

        val artObjecMap = mapOf(
            "objectNumber" to expected.objectNumber,
            "title" to expected.title,
            "principalOrFirstMaker" to expected.principalOrFirstMaker,
            "webImage" to mapOf("url" to expected.webImage),
            "dating" to mapOf("presentingDate" to expected.presentingDate),
            "acquisition" to mapOf("creditLine" to expected.creditLine)
        )
        val plaqueDescriptionMap = mapOf("plaqueDescription" to expected.plaqueDescription!!)
        val generated = RijksArtObjectDetailAdapter().fromJson(RijksArtObjectDetail(artObjecMap, plaqueDescriptionMap))
        assertEquals(expected, generated)
    }

    @Test
    fun `test that optional values are turned to empty strings when null`() {
        val expected = RijksArtObject("123")
        val artObjecMap = mapOf(
            "objectNumber" to expected.objectNumber,
            "title" to null,
            "principalOrFirstMaker" to null,
            "webImage" to mapOf("url" to null),
            "dating" to mapOf("presentingDate" to null),
            "acquisition" to mapOf("creditLine" to null)
        )
        val plaqueDescriptionMap = mapOf("plaqueDescription" to expected.plaqueDescription!!)
        val generated = RijksArtObjectDetailAdapter().fromJson(RijksArtObjectDetail(artObjecMap, plaqueDescriptionMap))
        assertEquals(expected, generated)
    }
}
