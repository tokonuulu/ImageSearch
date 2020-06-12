package com.example.imagesearch.ui.image.query.item

import android.util.Log
import com.example.imagesearch.R
import com.example.imagesearch.data.db.entity.ImageDescription
import com.example.imagesearch.internals.glide.GlideApp
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.image_list_element_layout.*

class ImageQueryItem(
    val imageDescription: ImageDescription

) : Item() {

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.apply {
            sitename_text_view.text = imageDescription.displaySitename
            date_text_view.text = imageDescription.datetime

            if (imageDescription.isFavorite) {
                favoriteIcon.tag = 2
                favoriteIcon.setImageResource(R.drawable.ic_favorite_pink_32)
            } else {
                favoriteIcon.tag = 1
                favoriteIcon.setImageResource(R.drawable.ic_favorite_border_32)
            }

            updateImage()
        }
    }

    override fun getLayout() = R.layout.image_list_element_layout

    private fun GroupieViewHolder.updateImage() {
        GlideApp.with(this.containerView)
            .load(imageDescription.thumbnailUrl)
            .into(image_view)
    }
}