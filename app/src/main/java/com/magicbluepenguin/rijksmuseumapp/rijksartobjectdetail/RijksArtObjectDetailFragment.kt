package com.magicbluepenguin.rijksmuseumapp.rijksartobjectdetail

import android.content.res.Resources
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.magicbluepenguin.network.RijksMuseumNetworkErrorResponse
import com.magicbluepenguin.network.RijksMuseumServerErrorResponse
import com.magicbluepenguin.rijksmuseumapp.R
import com.magicbluepenguin.rijksmuseumapp.base.BaseFragment
import com.magicbluepenguin.rijksmuseumapp.dagger.RijksMuseumAppComponent
import com.magicbluepenguin.rijksmuseumapp.databinding.FragmentArtObjectDetailBinding

internal class RijksArtObjectDetailFragment : BaseFragment() {

    companion object {
        private const val ACTION_BAR_HEIGHT_RATIO = 0.7
    }

    private val args: RijksArtObjectDetailFragmentArgs by navArgs()
    private val artObjectDetailViewModel by viewModels<RijksArtObjectDetailViewModel>()

    override fun onAppComponentReady(rijksMuseumAppComponent: RijksMuseumAppComponent) {
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
                // Make the image 70% of the height of the screen. Use static displayMetrics because
                // there will be cases in which the height of the root view is not set
                height = (Resources.getSystem().displayMetrics.heightPixels * ACTION_BAR_HEIGHT_RATIO).toInt()
                appBar.layoutParams = this
            }

            if (args.artObjectWebAddressr.isEmpty()) {
                itemViewArtObjectFab.visibility = View.GONE
            } else {
                itemViewArtObjectFab.setOnClickListener {
                    CustomTabsIntent.Builder()
                        .setToolbarColor(requireActivity().getColor(R.color.colorPrimary))
                        .build().launchUrl(requireContext(), Uri.parse(args.artObjectWebAddressr))
                }
            }

            rijksArtObjectDetailViewModel = artObjectDetailViewModel
            lifecycleOwner = this@RijksArtObjectDetailFragment

            artObjectDetailViewModel.errorLiveData.observe(
                viewLifecycleOwner,
                Observer {
                    val errorString = when (it) {
                        is RijksMuseumNetworkErrorResponse -> getString(R.string.network_error_message)
                        is RijksMuseumServerErrorResponse -> getString(R.string.generic_error_message)
                    }
                    Snackbar.make(
                        requireActivity().findViewById(android.R.id.content),
                        errorString, Snackbar.LENGTH_LONG
                    ).show()
                    requireView().findNavController().navigateUp()
                }
            )
        }.root
    }
}
