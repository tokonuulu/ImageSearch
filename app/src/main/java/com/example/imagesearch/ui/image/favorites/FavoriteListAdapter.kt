package com.example.imagesearch.ui.image.favorites

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.imagesearch.R
import com.example.imagesearch.data.db.entity.ImageDescription
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.favorite_list_element_layout.view.*
import kotlin.properties.Delegates

class FavoriteListAdapter(private val itemClickListener: OnItemClickListener) :
    RecyclerView.Adapter<FavoriteImageViewHolder>() {

    private var list: List<ImageDescription> by Delegates.observable(emptyList()) { property, oldValue, newValue ->
        notifyChanges(oldValue, newValue)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteImageViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return FavoriteImageViewHolder(
            inflater.inflate(
                R.layout.favorite_list_element_layout,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: FavoriteImageViewHolder, position: Int) {
        holder.bind(list[position], itemClickListener)
    }

    override fun getItemCount(): Int = list.size

    fun updateList(newList: List<ImageDescription>) {
        list = newList
    }

    private fun notifyChanges(oldList: List<ImageDescription>, newList: List<ImageDescription>) {
        val result = DiffUtil.calculateDiff(object : DiffUtil.Callback() {

            override fun getOldListSize(): Int {
                return oldList.size
            }

            override fun getNewListSize(): Int {
                return newList.size
            }

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return oldList[oldItemPosition].id.compareTo(newList[newItemPosition].id) == 0
            }

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                val oldItem = oldList[oldItemPosition]
                val newItem = newList[newItemPosition]

                return oldItem.isFavorite == newItem.isFavorite &&
                        oldItem.height == newItem.height &&
                        oldItem.width == newItem.width &&
                        oldItem.queryString.compareTo(newItem.queryString) == 0 &&
                        oldItem.thumbnailUrl.compareTo(newItem.thumbnailUrl) == 0 &&
                        oldItem.imageUrl.compareTo(newItem.imageUrl) == 0 &&
                        oldItem.datetime.compareTo(newItem.datetime) == 0 &&
                        oldItem.displaySitename.compareTo(newItem.displaySitename) == 0 &&
                        oldItem.id.compareTo(newItem.id) == 0

            }
        })
        result.dispatchUpdatesTo(this)
    }
}

class FavoriteImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(
        imageDescription: ImageDescription,
        itemClickListener: OnItemClickListener
    ) {
        itemView.favorite_icon.setImageResource(R.drawable.ic_favorite_pink_32)

        Picasso.get()
            .load(imageDescription.thumbnailUrl)
            .into(itemView.image_view_favorite)

        itemView.setOnClickListener {
            itemClickListener.onItemClicked(imageDescription)
        }
    }
}

interface OnItemClickListener {
    fun onItemClicked(imageDescription: ImageDescription)
}