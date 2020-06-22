package com.magicbluepenguin.rijksmuseumapp.rijksartobjectdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.magicbluepenguin.rijksmuseumapp.base.BaseFragment
import com.magicbluepenguin.rijksmuseumapp.dagger.RijksMuseumAppComponent
import com.magicbluepenguin.rijksmuseumapp.databinding.FragmentArtObjectDetailBinding

internal class RijksArtObjectDetailFragment : BaseFragment() {

    companion object {
        private const val ACTION_BAR_HEIGHT_RATIO = 0.7
    }

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
            (activity as AppCompatActivity).apply {
                setSupportActionBar(toolbar)
                title = null
                supportActionBar?.setDisplayHomeAsUpEnabled(true)
            }

            toolbar.setNavigationOnClickListener { view ->
                view.findNavController().navigateUp()
            }

            appBar.layoutParams.apply {
                height = (container!!.height * ACTION_BAR_HEIGHT_RATIO).toInt()
                appBar.layoutParams = this
            }

            rijksArtObjectDetailViewModel = artObjectDetailViewModel
            lifecycleOwner = this@RijksArtObjectDetailFragment
        }.root
    }
}
