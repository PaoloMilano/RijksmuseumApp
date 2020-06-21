package com.magicbluepenguin.rijksmuseumapp.rijksartobjectlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.magicbluepenguin.rijksmuseumapp.R
import com.magicbluepenguin.rijksmuseumapp.data.RijksArtObject
import kotlinx.android.synthetic.main.fragment_art_object_list.*

internal class RijksArtObjectListFragment : BaseFragment(R.layout.fragment_art_object_list) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = RijksArtObjectAdapter()
        artObjectRecyclerView.layoutManager = LinearLayoutManager(context)
        artObjectRecyclerView.adapter = adapter

        val rijksArtObjectListViewModel by viewModelso<RijksArtObjectListViewModel>()
        rijksArtObjectListViewModel.loadData()
        rijksArtObjectListViewModel.rijksArtObjectListLiveData.observe(
            viewLifecycleOwner,
            Observer {
                artObjectSwipeRefresh.isRefreshing = false
                adapter.submitList(it)
            }
        )
        artObjectSwipeRefresh.setOnRefreshListener { rijksArtObjectListViewModel.loadData() }
    }

    class RijksArtObjectItem(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(rijksArtObject: RijksArtObject) {
            itemView.findViewById<TextView>(R.id.itemViewArtObjectTitle).text = rijksArtObject.title
        }
    }

    class RijksArtObjectAdapter :
        PagedListAdapter<RijksArtObject, RijksArtObjectItem>(DIFF_CALLBACK) {

        override fun onBindViewHolder(holder: RijksArtObjectItem, position: Int) {
            val item = getItem(position)
            if (item != null) {
                holder.bind(item)
            } else {
                // Show placeholder
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = RijksArtObjectItem(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_view_art_object, parent, false)
        )

        companion object {
            private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<RijksArtObject>() {

                override fun areItemsTheSame(oldItem: RijksArtObject, newItem: RijksArtObject) =
                    oldItem == newItem

                override fun areContentsTheSame(oldItem: RijksArtObject, newItem: RijksArtObject) =
                    areItemsTheSame(oldItem, newItem)
            }
        }
    }
}
