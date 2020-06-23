package com.magicbluepenguin.rijksmuseumapp.rijksartobjectdetail

import com.magicbluepenguin.network.*
import com.magicbluepenguin.network.data.RijksArtObject
import io.mockk.*
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
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
        val testDispatcher = TestCoroutineDispatcher()
        Dispatchers.setMain(testDispatcher)
        mockkStatic(Dispatchers::class)
        every { Dispatchers.IO } answers { testDispatcher }
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkAll()
    }

    @Test
    fun `ensure ViewModel calls service wrapper with correct parameters`() = runBlockingTest {
        coEvery { mockRijksMuseumCollectionsServiceWrapper.getArtObject(any()) } coAnswers {
            RijksMuseumCollectionObjectDetailFailResponse(
                RijksMuseumNetworkErrorResponse
            )
        }
        val objectNumberParam = "123ABC"
        RijksArtObjectDetailViewModel(objectNumberParam, mockRijksMuseumCollectionsServiceWrapper)
        coVerify { mockRijksMuseumCollectionsServiceWrapper.getArtObject(objectNumberParam) }
        confirmVerified(mockRijksMuseumCollectionsServiceWrapper)
    }

    @Test
    fun `ensure ViewModel propagates artObject result to live data`() = runBlockingTest {
        val expected = RijksArtObject("123", "Title", "Maker")
        coEvery { mockRijksMuseumCollectionsServiceWrapper.getArtObject(any()) } coAnswers {
            RijksMuseumCollectionObjectDetailSuccessResponse(expected)
        }
        val returned = RijksArtObjectDetailViewModel("", mockRijksMuseumCollectionsServiceWrapper).rijksArtObjectLiveData.value
        assertEquals(expected, returned)
    }

    @Test
    fun `ensure ViewModel propagates network errors to liveData`() = runBlockingTest {
        coEvery { mockRijksMuseumCollectionsServiceWrapper.getArtObject(any()) } coAnswers {
            RijksMuseumCollectionObjectDetailFailResponse(
                RijksMuseumNetworkErrorResponse
            )
        }
        val networkErrorResponse = RijksArtObjectDetailViewModel("", mockRijksMuseumCollectionsServiceWrapper).errorLiveData.value
        assertEquals(networkErrorResponse, RijksMuseumNetworkErrorResponse)
    }

    @Test
    fun `ensure ViewModel propagates server errors to liveData`() = runBlockingTest {
        coEvery { mockRijksMuseumCollectionsServiceWrapper.getArtObject(any()) } coAnswers {
            RijksMuseumCollectionObjectDetailFailResponse(
                RijksMuseumServerErrorResponse
            )
        }
        val serverErrorResponse = RijksArtObjectDetailViewModel("", mockRijksMuseumCollectionsServiceWrapper).errorLiveData.value
        assertEquals(serverErrorResponse, RijksMuseumServerErrorResponse)
    }
}
