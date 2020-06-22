package com.magicbluepenguin.rijksmuseumapp.rijksartobjectlist

import androidx.lifecycle.Observer
import androidx.paging.PagedList
import com.magicbluepenguin.rijksmuseumapp.data.RijksArtObject
import com.magicbluepenguin.rijksmuseumapp.network.RijksMuseumCollectionsServiceWrapper
import com.magicbluepenguin.rijksmuseumapp.network.RijksMuseumNetworkErrorResponse
import io.mockk.*
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
internal class RijksArtObjectListViewModelTest {

    @Before
    fun setUp() {
        Dispatchers.setMain(TestCoroutineDispatcher())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `ensure the ViewModel loads new data on initialisation`() {
        val mockObserver = mockk<Observer<PagedList<RijksArtObject>>>(relaxed = true)
        val mockRijksMuseumCollectionsServiceWrapper = mockk<RijksMuseumCollectionsServiceWrapper>()
        val viewModel = RijksArtObjectListViewModel(RijksArtObjectListRepository(mockRijksMuseumCollectionsServiceWrapper)).apply {
            rijksArtObjectListLiveData.observeForever(mockObserver)
        }
        coVerify {
            // Should fetch the first page on initialisation
            mockRijksMuseumCollectionsServiceWrapper.listArtObjects(1, any())
        }
        confirmVerified(mockRijksMuseumCollectionsServiceWrapper)

        viewModel.rijksArtObjectListLiveData.removeObserver(mockObserver)
    }

    @Test
    fun `ensure the ViewModel exposes errors from data source`() {
        val mockObserver = mockk<Observer<PagedList<RijksArtObject>>>(relaxed = true)
        val mockRijksArtObjectListRepository = mockk<RijksArtObjectListRepository>(relaxed = true)
        val callbackSlot = slot<(RijksArtObjectListDataState) -> Unit>()

        val viewModel = RijksArtObjectListViewModel(mockRijksArtObjectListRepository).apply {
            rijksArtObjectListLiveData.observeForever(mockObserver)
        }

        coVerify { mockRijksArtObjectListRepository.getRijksArtObjectDataSource(any(), capture(callbackSlot)) }

        val error = RijksArtObjectListDataSourceError(RijksMuseumNetworkErrorResponse, true)
        callbackSlot.captured.invoke(error)
        assertEquals(error, viewModel.errorLiveData.value)

        viewModel.rijksArtObjectListLiveData.removeObserver(mockObserver)
    }

    @Test
    fun `ensure the ViewModel exposes initialisation state from data source`() {
        val mockInitialisationObserver = mockk<Observer<Boolean>>(relaxed = true)
        val mockPageObserver = mockk<Observer<PagedList<RijksArtObject>>>(relaxed = true)
        val mockRijksArtObjectListRepository = mockk<RijksArtObjectListRepository>(relaxed = true)
        val callbackSlot = slot<(RijksArtObjectListDataState) -> Unit>()

        val viewModel = RijksArtObjectListViewModel(mockRijksArtObjectListRepository).apply {
            rijksArtObjectListLiveData.observeForever(mockPageObserver)
            isInitialisingLiveData.observeForever(mockInitialisationObserver)
        }

        verify { mockRijksArtObjectListRepository.getRijksArtObjectDataSource(any(), capture(callbackSlot)) }

        callbackSlot.captured.invoke(RijksArtObjectListDataStateInitialising(true))
        callbackSlot.captured.invoke(RijksArtObjectListDataStateInitialising(false))
        verifySequence {
            mockInitialisationObserver.onChanged(true)
            mockInitialisationObserver.onChanged(false)
        }

        viewModel.isInitialisingLiveData.removeObserver(mockInitialisationObserver)
        viewModel.rijksArtObjectListLiveData.removeObserver(mockPageObserver)
    }
}
