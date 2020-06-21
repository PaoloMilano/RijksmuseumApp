package com.magicbluepenguin.rijksmuseumapp.rijksartobjectlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.Config
import androidx.paging.PagedList
import androidx.paging.toLiveData
import com.magicbluepenguin.rijksmuseumapp.data.RijksArtObject
import com.magicbluepenguin.rijksmuseumapp.network.RijksMuseumRetrofitServiceProvider
import com.magicbluepenguin.rijksmuseumapp.rijksartobjectlist.repository.RijksArtObjectRepository

internal class RijksArtObjectListViewModel : ViewModel() {

    private val dataSourceFactory = RijksArtObjectRepository(
        RijksMuseumRetrofitServiceProvider.getRijksMuseumServiceWrapper(
            "0fiuZFh4",
            "en"
        )
    )

    val rijksArtObjectListLiveData: LiveData<PagedList<RijksArtObject>> =
        dataSourceFactory.rijksObjectDataSourceFactory.toLiveData(
            Config(
                pageSize = 100,
                prefetchDistance = 200,
                enablePlaceholders = false,
                maxSize = 1000
            )
        )

    fun loadData() {
        dataSourceFactory.loadData()
    }

    override fun onCleared() {
        super.onCleared()
        dataSourceFactory.onCleared()
    }
}
