package com.magicbluepenguin.rijksmuseumapp.rijksartobjectdetail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.magicbluepenguin.rijksmuseumapp.data.RijksArtObject
import com.magicbluepenguin.rijksmuseumapp.network.RijksMuseumCollectionObjectDetailFailResponse
import com.magicbluepenguin.rijksmuseumapp.network.RijksMuseumCollectionObjectDetailSuccessResponse
import com.magicbluepenguin.rijksmuseumapp.network.RijksMuseumCollectionsServiceWrapper
import com.magicbluepenguin.rijksmuseumapp.network.RijksMuseumErrorResponse
import kotlinx.coroutines.launch
import javax.inject.Inject

internal class RijksArtObjectDetailViewModel @Inject constructor(
    private val artObjectNumber: String,
    private val rijksMuseumCollectionsServiceWrapper: RijksMuseumCollectionsServiceWrapper
) : ViewModel() {

    val rijksArtObjectLiveData = MutableLiveData<RijksArtObject>()
    val errorLiveData = MutableLiveData<RijksMuseumErrorResponse>()

    init {
        viewModelScope.launch {
            rijksMuseumCollectionsServiceWrapper.getArtObject(artObjectNumber).let {
                when (it) {
                    is RijksMuseumCollectionObjectDetailSuccessResponse -> rijksArtObjectLiveData.postValue(it.artObject)
                    is RijksMuseumCollectionObjectDetailFailResponse -> errorLiveData.postValue(it.error)
                }
            }
        }
    }
}
