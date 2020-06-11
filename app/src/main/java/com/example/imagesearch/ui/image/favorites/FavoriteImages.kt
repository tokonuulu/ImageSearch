package com.example.imagesearch.ui.image.favorites

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.imagesearch.R

class FavoriteImages : Fragment() {

    companion object {
        fun newInstance() = FavoriteImages()
    }

    private lateinit var viewModel: FavoriteImagesViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.favorite_images_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(FavoriteImagesViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
