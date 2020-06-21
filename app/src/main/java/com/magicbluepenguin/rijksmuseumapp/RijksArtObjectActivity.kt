package com.magicbluepenguin.rijksmuseumapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.magicbluepenguin.rijksmuseumapp.dagger.rijksartobjectcomponent.DaggerRijksArtObjectComponent
import com.magicbluepenguin.rijksmuseumapp.dagger.rijksartobjectcomponent.RijksArtObjectComponent
import com.magicbluepenguin.rijksmuseumapp.network.RijksMuseumRetrofitServiceProvider
import com.magicbluepenguin.rijksmuseumapp.rijksartobjectlist.BaseFragment
import com.magicbluepenguin.rijksmuseumapp.rijksartobjectlist.FragmentInjector
import com.magicbluepenguin.rijksmuseumapp.rijksartobjectlist.RijksArtObjectListFragment
import kotlinx.android.synthetic.main.activity_scrolling.*

class RijksArtObjectActivity : AppCompatActivity(R.layout.activity_scrolling), FragmentInjector {

    private lateinit var rijksArtObjectComponent: RijksArtObjectComponent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(toolbar)
        supportFragmentManager.beginTransaction()
            .replace(R.id.activityContentView, RijksArtObjectListFragment()).commit()

        rijksArtObjectComponent = DaggerRijksArtObjectComponent
            .builder()
            .withRijksMuseumCollectionsServiceWrapper(
                RijksMuseumRetrofitServiceProvider.getRijksMuseumServiceWrapper(
                    "0fiuZFh4",
                    getString(R.string.app_language)
                )
            )
            .build()
    }

    override fun inject(baseFragment: BaseFragment) {
        rijksArtObjectComponent.inject(baseFragment)
    }
}
