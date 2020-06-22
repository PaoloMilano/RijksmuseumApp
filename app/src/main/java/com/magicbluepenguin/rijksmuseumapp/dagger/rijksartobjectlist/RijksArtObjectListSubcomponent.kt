package com.magicbluepenguin.rijksmuseumapp.dagger.rijksartobjectlist

import com.magicbluepenguin.rijksmuseumapp.base.BaseFragment
import dagger.Subcomponent

@Subcomponent(modules = [RijksArtObjectListViewModelFactoryModule::class])
internal interface RijksArtObjectListSubcomponent {

    @Subcomponent.Builder
    interface Builder {
        fun build(): RijksArtObjectListSubcomponent
    }

    fun inject(baseFragment: BaseFragment)
}
