package com.magicbluepenguin.rijksmuseumapp.rijksartobjectdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.magicbluepenguin.rijksmuseumapp.base.BaseFragment
import com.magicbluepenguin.rijksmuseumapp.dagger.rijksartobjectcomponent.RijksMuseumAppComponent
import com.magicbluepenguin.rijksmuseumapp.databinding.FragmentArtObjectDetailBinding

internal class RijksArtObjectDetailFragment : BaseFragment() {

    private val artObjectDetailViewModel by viewModels<RijksArtObjectDetailViewModel>()

    override fun onAppComponentReady(rijksMuseumAppComponent: RijksMuseumAppComponent) {
        val args: RijksArtObjectDetailFragmentArgs by navArgs()
        rijksMuseumAppComponent.getArtObjectDetailSubcomponent()
            .withArtObjectNumber(args.artObjectNumber)
            .build()
            .inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentArtObjectDetailBinding.inflate(layoutInflater).apply {
            rijksArtObjectDetailViewModel = artObjectDetailViewModel
            lifecycleOwner = this@RijksArtObjectDetailFragment
        }.root
    }
}
