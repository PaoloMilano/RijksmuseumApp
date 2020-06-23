package com.magicbluepenguin.rijksmuseumapp

import androidx.appcompat.app.AppCompatActivity
import com.magicbluepenguin.network.RijksMuseumRetrofitServiceProvider
import com.magicbluepenguin.rijksmuseumapp.base.FragmentInjector
import com.magicbluepenguin.rijksmuseumapp.dagger.DaggerRijksMuseumAppComponent

internal class RijksArtObjectActivity :
    AppCompatActivity(R.layout.activity_rijks_art_object),
    FragmentInjector {

    override val rijksMuseumAppComponent by lazy {
        DaggerRijksMuseumAppComponent
            .builder()
            .withRijksMuseumCollectionsServiceWrapper(
                RijksMuseumRetrofitServiceProvider.getRijksMuseumServiceWrapper(
                    "0fiuZFh4",
                    getString(R.string.app_language)
                )
            )
            .build()
    }
}
