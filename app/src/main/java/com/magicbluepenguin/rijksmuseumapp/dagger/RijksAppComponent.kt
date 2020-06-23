package com.magicbluepenguin.rijksmuseumapp.dagger

import com.magicbluepenguin.network.RijksMuseumCollectionsServiceWrapper
import com.magicbluepenguin.rijksmuseumapp.dagger.rijksartobjectdetail.RijksArtObjectDetailSubcomponent
import com.magicbluepenguin.rijksmuseumapp.dagger.rijksartobjectlist.RijksArtObjectListSubcomponent
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
