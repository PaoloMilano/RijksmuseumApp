package com.magicbluepenguin.rijksmuseumapp.rijksartobjectdetail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.magicbluepenguin.network.RijksMuseumCollectionObjectDetailFailResponse
import com.magicbluepenguin.network.RijksMuseumCollectionObjectDetailSuccessResponse
import com.magicbluepenguin.network.RijksMuseumCollectionsServiceWrapper
import com.magicbluepenguin.network.RijksMuseumErrorResponse
import com.magicbluepenguin.network.data.RijksArtObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

internal class RijksArtObjectDetailViewModel @Inject constructor(
    private val artObjectNumber: String,
    private val rijksMuseumCollectionsServiceWrapper: RijksMuseumCollectionsServiceWrapper
) : ViewModel() {

    val rijksArtObjectLiveData = MutableLiveData<RijksArtObject>()
    val errorLiveData = MutableLiveData<RijksMuseumErrorResponse>()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            rijksMuseumCollectionsServiceWrapper.getArtObject(artObjectNumber).let {
                when (it) {
                    is RijksMuseumCollectionObjectDetailSuccessResponse -> rijksArtObjectLiveData.postValue(it.artObject)
                    is RijksMuseumCollectionObjectDetailFailResponse -> errorLiveData.postValue(it.error)
                }
            }
        }
    }
}
