package com.magicbluepenguin.rijksmuseumapp.dagger.rijksartobjectlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.magicbluepenguin.rijksmuseumapp.rijksartobjectlist.RijksArtObjectListViewModel
import javax.inject.Inject
import javax.inject.Provider

internal class RijksArtObjectListViewModelFactory @Inject constructor(
    private val rijksArtObjectListViewModel: Provider<RijksArtObjectListViewModel>
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when (modelClass) {
            RijksArtObjectListViewModel::class.java -> rijksArtObjectListViewModel.get()
            else -> throw IllegalArgumentException("Type not supported by this ViewModel factory: ${modelClass.name}")
        } as T
    }
}
