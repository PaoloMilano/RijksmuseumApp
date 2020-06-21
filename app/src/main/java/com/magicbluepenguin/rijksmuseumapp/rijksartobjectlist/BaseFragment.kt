package com.magicbluepenguin.rijksmuseumapp.rijksartobjectlist

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import javax.inject.Inject

interface FragmentInjector {
    fun inject(baseFragment: BaseFragment)
}

open class BaseFragment @JvmOverloads constructor(layoutId: Int = 0) : Fragment(layoutId) {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    inline fun <reified VM : ViewModel> viewModelso(
        noinline ownerProducer: () -> ViewModelStoreOwner = { this }
    ) = viewModels<VM>(ownerProducer) {
        viewModelFactory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val fragmentInjector = requireActivity() as? FragmentInjector
        if (fragmentInjector != null) {
            fragmentInjector.inject(this)
        } else {
            throw IllegalStateException("BaseFragment instances must be contained within activities that implement FragmentInjector")
        }
    }
}
