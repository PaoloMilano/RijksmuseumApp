package com.magicbluepenguin.rijksmuseumapp.dagger.rijksartobjectcomponent

import com.magicbluepenguin.rijksmuseumapp.network.RijksMuseumCollectionsServiceWrapper
import com.magicbluepenguin.rijksmuseumapp.rijksartobjectlist.BaseFragment
import dagger.BindsInstance
import dagger.Component

@Component(modules = [RijksArtObjectViewModelFactoryModule::class])
internal interface RijksArtObjectComponent {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun withRijksMuseumCollectionsServiceWrapper(rijksmuseumcollectionsservicewrapper: RijksMuseumCollectionsServiceWrapper): Builder

        fun build(): RijksArtObjectComponent
    }

    fun inject(baseFragment: BaseFragment)
}
