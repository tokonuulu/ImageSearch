package com.example.imagesearch.ui.image.favorites

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager

import com.example.imagesearch.R
import com.example.imagesearch.data.db.entity.ImageDescription
import com.example.imagesearch.ui.base.ScopedFragment
import kotlinx.android.synthetic.main.image_query_fragment.*
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

class FavoriteImagesFragment : ScopedFragment(), KodeinAware, OnItemClickListener {

    override val kodein by closestKodein()
    private val viewModelFactory: FavoriteImagesViewModelFactory by instance()

    private lateinit var viewModel: FavoriteImagesViewModel
    private lateinit var imageAdapter: FavoriteListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.favorite_images_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(FavoriteImagesViewModel::class.java)

        imageAdapter = FavoriteListAdapter(this)

        recyclerView.apply {
            layoutManager = GridLayoutManager(this@FavoriteImagesFragment.context, 2)
            adapter = imageAdapter
        }

        launch {
            val allFavorites = viewModel.allFavorites.await()

            allFavorites.observe(viewLifecycleOwner, Observer { list ->
                if (list == null) return@Observer
                group_loading.visibility = View.GONE
                imageAdapter.updateList(list)

            })

        }
    }

    override fun onItemClicked(imageDescription: ImageDescription) {
        viewModel.onImageClicked(imageDescription)
    }
}
