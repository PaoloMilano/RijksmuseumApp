package com.magicbluepenguin.rijksmuseumapp.rijksartobjectdetail

import com.magicbluepenguin.rijksmuseumapp.data.RijksArtObject
import com.magicbluepenguin.rijksmuseumapp.network.*
import io.mockk.coEvery
import io.mockk.coVerifySequence
import io.mockk.mockk
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
internal class RijksArtObjectDetailViewModelTest {

    private val mockRijksMuseumCollectionsServiceWrapper = mockk<RijksMuseumCollectionsServiceWrapper>(relaxed = true)

    @Before
    fun setUp() {
        Dispatchers.setMain(TestCoroutineDispatcher())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `ensure ViewModel calls service wrapper with correct parameters`() {
        coEvery { mockRijksMuseumCollectionsServiceWrapper.getArtObject(any()) } coAnswers { RijksMuseumCollectionObjectDetailFailResponse(RijksMuseumNetworkErrorResponse) }
        val objectNumberParam = "123ABC"
        RijksArtObjectDetailViewModel(objectNumberParam, mockRijksMuseumCollectionsServiceWrapper)
        coVerifySequence { mockRijksMuseumCollectionsServiceWrapper.getArtObject(objectNumberParam) }
    }

    @Test
    fun `ensure ViewModel propagates artObject result to live data`() {
        val expected = RijksArtObject("123", "Title", "Maker")
        coEvery { mockRijksMuseumCollectionsServiceWrapper.getArtObject(any()) } coAnswers {
            RijksMuseumCollectionObjectDetailSuccessResponse(expected)
        }
        val returned = RijksArtObjectDetailViewModel("", mockRijksMuseumCollectionsServiceWrapper).rijksArtObjectLiveData.value
        assertEquals(expected, returned)
    }

    @Test
    fun `ensure ViewModel propagates network errors to liveData`() {
        coEvery { mockRijksMuseumCollectionsServiceWrapper.getArtObject(any()) } coAnswers { RijksMuseumCollectionObjectDetailFailResponse(RijksMuseumNetworkErrorResponse) }
        val networkErrorResponse = RijksArtObjectDetailViewModel("", mockRijksMuseumCollectionsServiceWrapper).errorLiveData.value
        assertEquals(networkErrorResponse, RijksMuseumNetworkErrorResponse)
    }

    @Test
    fun `ensure ViewModel propagates server errors to liveData`() {
        coEvery { mockRijksMuseumCollectionsServiceWrapper.getArtObject(any()) } coAnswers { RijksMuseumCollectionObjectDetailFailResponse(RijksMuseumServerErrorResponse) }
        val serverErrorResponse = RijksArtObjectDetailViewModel("", mockRijksMuseumCollectionsServiceWrapper).errorLiveData.value
        assertEquals(serverErrorResponse, RijksMuseumServerErrorResponse)
    }
}
