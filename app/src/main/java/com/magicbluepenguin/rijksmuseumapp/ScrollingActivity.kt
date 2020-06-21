package com.magicbluepenguin.rijksmuseumapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.magicbluepenguin.rijksmuseumapp.rijksartobjectlist.RijksArtObjectListFragment
import kotlinx.android.synthetic.main.activity_scrolling.*

internal class ScrollingActivity : AppCompatActivity(R.layout.activity_scrolling) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(toolbar)
        supportFragmentManager.beginTransaction()
            .replace(R.id.activityContentView, RijksArtObjectListFragment()).commit()
    }
}
