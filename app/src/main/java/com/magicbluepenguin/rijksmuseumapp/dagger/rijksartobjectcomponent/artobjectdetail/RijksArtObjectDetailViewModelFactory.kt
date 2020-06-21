package com.magicbluepenguin.rijksmuseumapp.dagger.rijksartobjectcomponent.artobjectlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.magicbluepenguin.rijksmuseumapp.rijksartobjectdetail.RijksArtObjectDetailViewModel
import javax.inject.Inject
import javax.inject.Provider

internal class RijksArtObjectDetailViewModelFactory @Inject constructor(
    private val rijksArtObjectDetailViewModel: Provider<RijksArtObjectDetailViewModel>
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when (modelClass) {
            RijksArtObjectDetailViewModel::class.java -> rijksArtObjectDetailViewModel.get()
            else -> TODO("Missing viewModel $modelClass")
        } as T
    }
}
