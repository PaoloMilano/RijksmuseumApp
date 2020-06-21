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

    companion object {
        private const val PAGE_SIZE = 100
        private const val MAX_LIST_SIZE = 1000
    }

    val rijksArtObjectListLiveData: LiveData<PagedList<RijksArtObject>> =
        dataSourceFactory.rijksObjectDataSourceFactory.toLiveData(
            Config(
                pageSize = PAGE_SIZE,
                enablePlaceholders = false,
                maxSize = MAX_LIST_SIZE
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
