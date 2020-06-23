package com.magicbluepenguin.rijksmuseumapp.rijksartobjectlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.magicbluepenguin.network.data.RijksArtObject
import javax.inject.Inject

internal class RijksArtObjectListViewModel @Inject constructor(private val rijksArtObjectListRepository: RijksArtObjectListRepository) :
    ViewModel() {

    companion object {
        private const val PAGE_SIZE = 100
        private const val MAX_LIST_SIZE = 1000
    }

    private val sourceLiveData = MutableLiveData<PositionalDataSource<RijksArtObject>>()

    val isInitialisingLiveData = MutableLiveData<Boolean>()
    val errorLiveData = MutableLiveData<RijksArtObjectListDataSourceError>()
    val rijksArtObjectListLiveData: LiveData<PagedList<RijksArtObject>> =
        object :
            DataSource.Factory<Int, RijksArtObject>() {
            override fun create(): DataSource<Int, RijksArtObject> {
                val dataSource = rijksArtObjectListRepository.getRijksArtObjectDataSource(viewModelScope) {
                    when (it) {
                        is RijksArtObjectListDataSourceError -> errorLiveData.postValue(it)
                        is RijksArtObjectListDataStateInitialising -> isInitialisingLiveData.postValue(it.isInitialising)
                    }
                }
                sourceLiveData.postValue(dataSource)
                return dataSource
            }
        }.toLiveData(
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
        sourceLiveData.value?.invalidate()
    }
}
