package com.magicbluepenguin.rijksmuseumapp.dagger.rijksartobjectcomponent.artobjectlist

import com.magicbluepenguin.rijksmuseumapp.base.BaseFragment
import dagger.Subcomponent

@Subcomponent(modules = [RijksArtObjectDetailViewModelFactoryModule::class])
internal interface RijksArtObjectDetailSubcomponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): RijksArtObjectDetailSubcomponent
    }

    fun inject(baseFragment: BaseFragment)
}
