package com.magicbluepenguin.rijksmuseumapp.rijksartobjectlist

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.magicbluepenguin.network.RijksMuseumNetworkErrorResponse
import com.magicbluepenguin.network.RijksMuseumServerErrorResponse
import com.magicbluepenguin.rijksmuseumapp.R
import com.magicbluepenguin.rijksmuseumapp.base.BaseFragment
import com.magicbluepenguin.rijksmuseumapp.dagger.RijksMuseumAppComponent
import com.magicbluepenguin.rijksmuseumapp.databinding.FragmentArtObjectListBinding

internal class RijksArtObjectListFragment : BaseFragment() {

    private val artObjectListViewModel by viewModels<RijksArtObjectListViewModel>()

    override fun onAppComponentReady(rijksMuseumAppComponent: RijksMuseumAppComponent) {
        rijksMuseumAppComponent.getArtObjectListSubcomponent()
            .build()
            .inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentArtObjectListBinding.inflate(inflater).apply {

            val fragmentToolbar = toolbar
            (activity as AppCompatActivity).apply {
                setSupportActionBar(fragmentToolbar)
                title = getString(R.string.app_name)
            }

            rijksArtObjectListViewModel = artObjectListViewModel

            val pagedArtObjectAdapter = RijksArtObjectListAdapter {
                RijksArtObjectListFragmentDirections.actionRijksArtObjectListFragmentToRijksArtObjectDetailFragment(
                    it.objectNumber, it.webLink ?: ""
                ).let {
                    // This error will occur then multiple calls to `navigate` happen in quick succession.
                    // The safety of encasing this in a try catch is based on the assumption that there is no other
                    // way in which we can get to this point in the code and have the wrong `NavDirections` object
                    try {
                        findNavController().navigate(it)
                    } catch (e: IllegalArgumentException) {
                        Log.w(this@RijksArtObjectListFragment::class.qualifiedName, e.localizedMessage)
                    }
                }
            }
            artObjectRecyclerView.adapter = pagedArtObjectAdapter
            artObjectRecyclerView.layoutManager = LinearLayoutManager(requireActivity())
            artObjectRecyclerView.addItemDecoration(
                DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL).apply {
                    requireActivity().getDrawable(R.drawable.horizontal_recyclervirew_divider)
                        ?.let {
                            setDrawable(it)
                        }
                }
            )

            artObjectListViewModel.isInitialisingLiveData.observe(
                viewLifecycleOwner,
                Observer { isInitialising ->
                    if (isInitialising) {
                        artObjectProgressBar.show()
                    } else {
                        artObjectProgressBar.hide()
                    }
                }
            )

            artObjectListViewModel.rijksArtObjectListLiveData.observe(
                viewLifecycleOwner,
                Observer {
                    artObjectErrorView.visibility = View.GONE
                    pagedArtObjectAdapter.submitList(it)
                    artObjectSwipeRefresh.isRefreshing = false
                }
            )

            artObjectListViewModel.errorLiveData.observe(
                viewLifecycleOwner,
                Observer {

                    if (it.isInitialisationError) {
                        artObjectErrorView.visibility = View.VISIBLE
                    }

                    val errorString = when (it.rijksMuseumErrorResponse) {
                        is RijksMuseumNetworkErrorResponse -> getString(R.string.network_error_message)
                        is RijksMuseumServerErrorResponse -> getString(R.string.generic_error_message)
                    }
                    Snackbar.make(
                        requireActivity().findViewById(android.R.id.content),
                        errorString, Snackbar.LENGTH_LONG
                    ).addCallback(object : BaseTransientBottomBar.BaseCallback<Snackbar>() {
                        override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                        }
                    }).show()
                }
            )
        }.root
    }
}
