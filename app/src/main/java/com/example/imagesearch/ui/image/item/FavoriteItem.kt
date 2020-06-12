package com.example.imagesearch.ui.image.item

import com.example.imagesearch.R
import com.example.imagesearch.data.db.entity.ImageDescription
import com.example.imagesearch.internals.glide.GlideApp
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.favorite_list_element_layout.*

class FavoriteItem (
    val imageDescription: ImageDescription
) : Item() {

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.apply {

            favorite_icon_favorite.tag = 2
            favorite_icon_favorite.setImageResource(R.drawable.ic_favorite_pink_32)

            updateImage()
        }
    }

    override fun getLayout() = R.layout.favorite_list_element_layout

    private fun GroupieViewHolder.updateImage() {
        GlideApp.with(this.containerView)
            .load(imageDescription.thumbnailUrl)
            .into(image_view_favorite)
    }
}