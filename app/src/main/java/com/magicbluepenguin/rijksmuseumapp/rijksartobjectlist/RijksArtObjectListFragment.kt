package com.magicbluepenguin.rijksmuseumapp.rijksartobjectlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.magicbluepenguin.rijksmuseumapp.R
import com.magicbluepenguin.rijksmuseumapp.base.BaseFragment
import com.magicbluepenguin.rijksmuseumapp.dagger.RijksMuseumAppComponent
import com.magicbluepenguin.rijksmuseumapp.databinding.FragmentArtObjectListBinding

internal class RijksArtObjectListFragment : BaseFragment() {

    val artObjectListViewModel by viewModels<RijksArtObjectListViewModel>()

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
            rijksArtObjectListViewModel = artObjectListViewModel

            val pagedArtObjectAdapter = RijksArtObjectListAdapter {
                RijksArtObjectListFragmentDirections.actionRijksArtObjectListFragmentToRijksArtObjectDetailFragment(
                    it.objectNumber
                ).let {
                    findNavController().navigate(it)
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

            artObjectListViewModel.rijksArtObjectListLiveData.observe(
                viewLifecycleOwner,
                Observer {
                    pagedArtObjectAdapter.submitList(it)
                }
            )
        }.root
    }
}
