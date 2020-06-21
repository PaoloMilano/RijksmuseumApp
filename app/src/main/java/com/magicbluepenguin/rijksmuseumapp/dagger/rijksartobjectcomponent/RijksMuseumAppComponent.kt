package com.magicbluepenguin.rijksmuseumapp.dagger.rijksartobjectcomponent

import com.magicbluepenguin.rijksmuseumapp.dagger.rijksartobjectcomponent.artobjectlist.RijksArtObjectDetailSubcomponent
import com.magicbluepenguin.rijksmuseumapp.dagger.rijksartobjectcomponent.artobjectlist.RijksArtObjectListSubcomponent
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

    fun getArtObjectListSubcomponent(): RijksArtObjectListSubcomponent.Factory

    fun getArtObjectDetailSubcomponent(): RijksArtObjectDetailSubcomponent.Factory
}

@Module(subcomponents = [RijksArtObjectListSubcomponent::class, RijksArtObjectDetailSubcomponent::class])
class RijksMuseumAppSubcomponents
