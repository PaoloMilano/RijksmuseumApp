package com.magicbluepenguin.rijksmuseumapp.dagger

import com.magicbluepenguin.rijksmuseumapp.dagger.rijksartobjectdetail.RijksArtObjectDetailSubcomponent
import com.magicbluepenguin.rijksmuseumapp.dagger.rijksartobjectlist.RijksArtObjectListSubcomponent
import com.magicbluepenguin.rijksmuseumapp.network.RijksMuseumCollectionsServiceWrapper
import dagger.BindsInstance
import dagger.Component
import dagger.Module

@Component(modules = [RijksMuseumAppSubcomponents::class])
internal interface RijksMuseumAppComponent {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun withRijksMuseumCollectionsServiceWrapper(rijksmuseumcollectionsservicewrapper: RijksMuseumCollectionsServiceWrapper): Builder

        fun build(): RijksMuseumAppComponent
    }

    fun getArtObjectListSubcomponent(): RijksArtObjectListSubcomponent.Builder

    fun getArtObjectDetailSubcomponent(): RijksArtObjectDetailSubcomponent.Builder
}

@Module(subcomponents = [RijksArtObjectListSubcomponent::class, RijksArtObjectDetailSubcomponent::class])
class RijksMuseumAppSubcomponents
