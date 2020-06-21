package com.magicbluepenguin.rijksmuseumapp.rijksartobjectdetail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.magicbluepenguin.rijksmuseumapp.data.RijksArtObject
import com.magicbluepenguin.rijksmuseumapp.network.RijksMuseumCollectionsServiceWrapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

internal class RijksArtObjectDetailViewModel @Inject constructor(
    private val artObjectNumber: String,
    private val rijksMuseumCollectionsServiceWrapper: RijksMuseumCollectionsServiceWrapper
) :
    ViewModel() {

    val rijksArtObjectLiveData = MutableLiveData<RijksArtObject>()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            rijksArtObjectLiveData.postValue(
                rijksMuseumCollectionsServiceWrapper.getArtObject(
                    artObjectNumber
                )
            )
        }
    }
}
