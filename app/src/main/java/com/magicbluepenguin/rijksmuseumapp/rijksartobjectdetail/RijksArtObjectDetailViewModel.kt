package com.magicbluepenguin.rijksmuseumapp.rijksartobjectdetail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.magicbluepenguin.rijksmuseumapp.data.RijksArtObject
import com.magicbluepenguin.rijksmuseumapp.network.RijksMuseumArtObjectDetailResponse
import com.magicbluepenguin.rijksmuseumapp.network.RijksMuseumCollectionsServiceWrapper
import com.magicbluepenguin.rijksmuseumapp.network.RijksMuseumErrorResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

internal class RijksArtObjectDetailViewModel @Inject constructor(
    private val artObjectNumber: String,
    private val rijksMuseumCollectionsServiceWrapper: RijksMuseumCollectionsServiceWrapper
) :
    ViewModel() {

    val rijksArtObjectLiveData = MutableLiveData<RijksArtObject>()
    val errorLiveData = MutableLiveData<RijksMuseumErrorResponse>()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            rijksMuseumCollectionsServiceWrapper.getArtObject(artObjectNumber).let {
                when (it) {
                    is RijksMuseumArtObjectDetailResponse -> rijksArtObjectLiveData.postValue(it.artObject)
                    is RijksMuseumErrorResponse -> errorLiveData.postValue(it)
                    else -> throw IllegalStateException("Response type cannot be handled by this component: ${it.javaClass.name}")
                }
            }
        }
    }
}
