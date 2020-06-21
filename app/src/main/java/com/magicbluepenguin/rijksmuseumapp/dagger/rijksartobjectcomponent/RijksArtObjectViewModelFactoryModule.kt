package com.magicbluepenguin.rijksmuseumapp.dagger.rijksartobjectcomponent

import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module

@Module
internal abstract class RijksArtObjectViewModelFactoryModule {

    @Binds
    abstract fun providesViewModelFactory(viewModelFactory: RijksArtObjectViewModelFactory): ViewModelProvider.Factory
}
