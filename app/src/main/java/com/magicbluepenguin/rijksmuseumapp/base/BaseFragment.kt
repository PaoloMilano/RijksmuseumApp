package com.magicbluepenguin.rijksmuseumapp.base

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.magicbluepenguin.rijksmuseumapp.dagger.RijksMuseumAppComponent
import javax.inject.Inject

internal interface FragmentInjector {
    val rijksMuseumAppComponent: RijksMuseumAppComponent
}

internal abstract class BaseFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    inline fun <reified VM : ViewModel> viewModels(
        noinline ownerProducer: () -> ViewModelStoreOwner = { this }
    ) = viewModels<VM>(ownerProducer) {
        viewModelFactory
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val fragmentInjector = requireActivity() as? FragmentInjector
        if (fragmentInjector != null) {
            onAppComponentReady(fragmentInjector.rijksMuseumAppComponent)
        } else {
            throw IllegalStateException("BaseFragment instances must be contained within activities that implement FragmentInjector")
        }
    }

    abstract fun onAppComponentReady(rijksMuseumAppComponent: RijksMuseumAppComponent)
}
