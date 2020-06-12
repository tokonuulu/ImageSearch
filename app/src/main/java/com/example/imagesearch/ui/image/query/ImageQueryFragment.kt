package com.example.imagesearch.ui.image.query

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.imagesearch.R
import com.example.imagesearch.data.db.entity.ImageDescription
import com.example.imagesearch.ui.base.ScopedFragment
import com.example.imagesearch.ui.image.query.item.ImageQueryItem
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import kotlinx.android.synthetic.main.image_list_element_layout.view.*
import kotlinx.android.synthetic.main.image_query_fragment.*
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware

import org.kodein.di.generic.instance
import org.kodein.di.android.x.closestKodein

class ImageQueryFragment : ScopedFragment(), KodeinAware {

    override val kodein by closestKodein()
    private val viewModelFactory: ImageQueryViewModelFactory by instance()

    private lateinit var viewModel: ImageQueryViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.image_query_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(ImageQueryViewModel::class.java)


        bindUI()

    }

    private fun bindUI() = launch {
        val imageQueryResult = viewModel.imageQueryResult.await()

        imageQueryResult.observe(viewLifecycleOwner, Observer { list ->
            if (list == null) return@Observer

            Log.e("QueryOBSERVER", "List changed $list")
            group_loading.visibility = View.GONE
            initRecyclerView(list.toImageQueryItems())
        })
    }

    private fun List<ImageDescription>.toImageQueryItems(): List<ImageQueryItem> {
        return this.map {
            ImageQueryItem(it)
        }
    }

    private fun initRecyclerView(items: List<ImageQueryItem>) {
        val groupAdapter = GroupAdapter<GroupieViewHolder>().apply {
            addAll(items)
        }

        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@ImageQueryFragment.context)
            adapter = groupAdapter
        }

        groupAdapter.setOnItemClickListener { item, view ->
            (item as? ImageQueryItem)?.let {
                if (view.favoriteIcon.tag == 1) {
                    view.favoriteIcon.tag = 2
                    view.favoriteIcon.setImageResource(R.drawable.ic_favorite_pink_32)
                } else {
                    view.favoriteIcon.tag = 1
                    view.favoriteIcon.setImageResource(R.drawable.ic_favorite_border_32)
                }

                viewModel.onImageClicked(it.imageDescription)
            }
        }
    }
}
