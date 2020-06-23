package com.magicbluepenguin.rijksmuseumapp.rijksartobjectlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.magicbluepenguin.network.data.RijksArtObject
import com.magicbluepenguin.rijksmuseumapp.databinding.ItemViewArtObjectBinding

internal class RijksArtObjectItem(private val itemViewArtObjectBinding: ItemViewArtObjectBinding) :
    RecyclerView.ViewHolder(itemViewArtObjectBinding.root) {
    fun bind(rijksArtObject: RijksArtObject) {
        itemViewArtObjectBinding.artObject = rijksArtObject
    }
}

internal class RijksArtObjectListAdapter(private val artObjectSelectionListener: (RijksArtObject) -> Unit) :
    PagedListAdapter<RijksArtObject, RijksArtObjectItem>(DIFF_CALLBACK) {

    override fun onBindViewHolder(holder: RijksArtObjectItem, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.bind(item)
            holder.itemView.setOnClickListener {
                artObjectSelectionListener.invoke(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        RijksArtObjectItem(
            ItemViewArtObjectBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
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
