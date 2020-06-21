package com.magicbluepenguin.rijksmuseumapp.dagger.rijksartobjectcomponent.artobjectlist

import com.magicbluepenguin.rijksmuseumapp.base.BaseFragment
import dagger.Subcomponent

@Subcomponent(modules = [RijksArtObjectListViewModelFactoryModule::class])
internal interface RijksArtObjectListSubcomponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): RijksArtObjectListSubcomponent
    }

    fun inject(baseFragment: BaseFragment)
}
