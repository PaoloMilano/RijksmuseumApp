package com.magicbluepenguin.rijksmuseumapp.rijksartobjectlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.Config
import androidx.paging.PagedList
import androidx.paging.toLiveData
import com.magicbluepenguin.rijksmuseumapp.data.RijksArtObject
import com.magicbluepenguin.rijksmuseumapp.rijksartobjectlist.repository.RijksArtObjectRepository
import javax.inject.Inject

internal class RijksArtObjectListViewModel @Inject constructor(private val dataSourceFactory: RijksArtObjectRepository) :
    ViewModel() {

    val rijksArtObjectListLiveData: LiveData<PagedList<RijksArtObject>> =
        dataSourceFactory.rijksObjectDataSourceFactory.toLiveData(
            Config(
                pageSize = 100,
                prefetchDistance = 200,
                enablePlaceholders = false,
                maxSize = 1000
            )
        )

    init {
        loadData()
    }

    fun loadData() {
        dataSourceFactory.loadData()
    }

    override fun onCleared() {
        super.onCleared()
        dataSourceFactory.onCleared()
    }
}
