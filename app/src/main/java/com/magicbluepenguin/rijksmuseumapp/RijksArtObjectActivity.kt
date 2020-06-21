package com.magicbluepenguin.rijksmuseumapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.magicbluepenguin.rijksmuseumapp.base.FragmentInjector
import com.magicbluepenguin.rijksmuseumapp.dagger.rijksartobjectcomponent.DaggerRijksMuseumAppComponent
import com.magicbluepenguin.rijksmuseumapp.network.RijksMuseumRetrofitServiceProvider
import kotlinx.android.synthetic.main.activity_scrolling.*

internal class RijksArtObjectActivity :
    AppCompatActivity(R.layout.activity_scrolling),
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(toolbar)

        if (savedInstanceState == null) {
//            supportFragmentManager.beginTransaction()
//                .replace(R.id.activityContentView, RijksArtObjectListFragment()).commit()
        }
    }
}
