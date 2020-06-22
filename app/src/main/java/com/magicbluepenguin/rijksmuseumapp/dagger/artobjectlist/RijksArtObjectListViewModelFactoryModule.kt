package com.magicbluepenguin.rijksmuseumapp.dagger.artobjectlist

import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module

@Module
internal abstract class RijksArtObjectListViewModelFactoryModule {

    @Binds
    abstract fun providesViewModelFactory(viewModelFactory: RijksArtObjectListViewModelFactory): ViewModelProvider.Factory
}
