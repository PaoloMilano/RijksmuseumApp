package com.magicbluepenguin.rijksmuseumapp.dagger.rijksartobjectdetail

import com.magicbluepenguin.rijksmuseumapp.base.BaseFragment
import dagger.BindsInstance
import dagger.Subcomponent

@Subcomponent(modules = [RijksArtObjectDetailViewModelFactoryModule::class])
internal interface RijksArtObjectDetailSubcomponent {

    @Subcomponent.Builder
    interface Builder {

        @BindsInstance
        fun withArtObjectNumber(artObjectNumber: String): Builder

        fun build(): RijksArtObjectDetailSubcomponent
    }

    fun inject(baseFragment: BaseFragment)
}
