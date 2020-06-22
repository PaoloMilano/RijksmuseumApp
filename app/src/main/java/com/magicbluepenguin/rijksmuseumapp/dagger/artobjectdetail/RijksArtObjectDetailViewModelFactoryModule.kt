package com.magicbluepenguin.rijksmuseumapp.dagger.artobjectdetail

import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module

@Module
internal abstract class RijksArtObjectDetailViewModelFactoryModule {

    @Binds
    abstract fun providesViewModelFactory(viewModelFactory: RijksArtObjectDetailViewModelFactory): ViewModelProvider.Factory
}
