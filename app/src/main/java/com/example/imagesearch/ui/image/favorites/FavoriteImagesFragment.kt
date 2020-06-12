package com.example.imagesearch.ui.image.favorites

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.imagesearch.R
import com.example.imagesearch.data.db.entity.ImageDescription
import com.example.imagesearch.ui.base.ScopedFragment
import com.example.imagesearch.ui.image.item.FavoriteItem
import com.example.imagesearch.ui.image.query.ImageQueryViewModel
import com.example.imagesearch.ui.image.query.ImageQueryViewModelFactory
import com.example.imagesearch.ui.image.query.item.ImageQueryItem
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import kotlinx.android.synthetic.main.image_list_element_layout.view.*
import kotlinx.android.synthetic.main.image_query_fragment.*
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

class FavoriteImagesFragment : ScopedFragment(), KodeinAware {

    override val kodein by closestKodein()
    private val viewModelFactory: FavoriteImagesViewModelFactory by instance()

    private lateinit var viewModel: FavoriteImagesViewModel

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


        val groupAdapter = GroupAdapter<GroupieViewHolder>()

        recyclerView.apply {
            layoutManager = GridLayoutManager(this@FavoriteImagesFragment.context, 2)
            adapter = groupAdapter
        }

        groupAdapter.setOnItemClickListener { item, view ->
            (item as? FavoriteItem)?.let{
                viewModel.onImageClicked(it.imageDescription)
            }
        }

        launch {
            val allFavorites = viewModel.allFavorites.await()

            allFavorites.observe(viewLifecycleOwner, Observer { list ->
                if (list == null) return@Observer

                Log.e("favoritesOBSERVER", "List is changed $list")
                group_loading.visibility = View.GONE
                groupAdapter.update(list.toFavoriteItems())

            })

        }
    }

    private fun List<ImageDescription>.toFavoriteItems(): List<FavoriteItem> {
        return this.map {
            FavoriteItem(it)
        }
    }
}
