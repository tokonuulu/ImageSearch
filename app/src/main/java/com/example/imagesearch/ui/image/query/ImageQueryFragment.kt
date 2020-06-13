package com.example.imagesearch.ui.image.query

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.provider.SearchRecentSuggestions
import android.util.Log
import android.view.*
import android.widget.SearchView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager

import com.example.imagesearch.R
import com.example.imagesearch.data.db.entity.ImageDescription
import com.example.imagesearch.ui.MainActivity
import com.example.imagesearch.ui.base.ScopedFragment
import kotlinx.android.synthetic.main.image_query_fragment.*
import org.kodein.di.KodeinAware

import org.kodein.di.generic.instance
import org.kodein.di.android.x.closestKodein

class ImageQueryFragment : ScopedFragment(), KodeinAware, OnItemClickListener {

    override val kodein by closestKodein()
    private val viewModelFactory: ImageQueryViewModelFactory by instance()

    private lateinit var imageViewModel: ImageQueryViewModel
    private lateinit var searchViewModel: SearchViewModel

    private lateinit var imageAdapter : ImageListAdapter

    companion object {
        const val FIRST_LAUNCH : String = "FIRST_LAUNCH"
    }

    private var isItTheFirstLaunch : Boolean = true


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        setHasOptionsMenu(true)
        imageAdapter = ImageListAdapter(this)
        isItFirstLaunchCheck(savedInstanceState)

        return inflater.inflate(R.layout.image_query_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        imageViewModel = ViewModelProvider((this.activity as MainActivity), viewModelFactory)
            .get(ImageQueryViewModel::class.java)

        searchViewModel = ViewModelProvider((this.activity as MainActivity))
            .get(SearchViewModel::class.java)

        initRecyclerView()
        startObservingSearchView()
        startObservingQueryResult()
    }

    private fun startObservingSearchView() {
        with(searchViewModel) {


            if(!isItTheFirstLaunch) {
                Log.e("Fragment relaunched", "loading new")
                //imageViewModel.loadNewImages(queryChangeEvent.value)
            }

            queryChangeEvent.observe(viewLifecycleOwner, Observer { newQuery ->
                Log.e("SearchObserver", "Query changed $newQuery")
                imageViewModel.loadNewImages(newQuery)
            })
        }
    }

    private fun startObservingQueryResult() {
        imageViewModel.imageList.observe(viewLifecycleOwner, Observer<List<ImageDescription>> {
            val newList = mutableListOf<ImageDescription>()
            it.forEach { item -> newList.add(item.copy()) }
            group_loading.visibility = View.GONE
            imageAdapter.updateList(newList)
        })
    }

    private fun initRecyclerView() {
        recyclerView.apply {
            layoutManager = GridLayoutManager(this@ImageQueryFragment.context, 2)
            adapter = imageAdapter
            setHasFixedSize(true)
            setItemViewCacheSize(20)
        }
    }

    override fun onItemClicked(imageDescription: ImageDescription) {
        imageViewModel.onImageClicked(imageDescription)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()

        inflater.inflate(R.menu.search_menu, menu)

        val searchView =
            SearchView((context as MainActivity).supportActionBar?.themedContext ?: context)
        menu.findItem(R.id.action_search).apply {
            setShowAsAction(MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW or MenuItem.SHOW_AS_ACTION_IF_ROOM)
            actionView = searchView
        }

        searchView.isIconifiedByDefault = false
        searchView.isQueryRefinementEnabled = true

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {

                // Saving queries
                SearchRecentSuggestions(
                    this@ImageQueryFragment.context,
                    SuggestionProvider.AUTHORITY,
                    SuggestionProvider.MODE
                )
                    .saveRecentQuery(query, null)

                // Notify the ViewModel
                group_loading.visibility = View.VISIBLE
                searchViewModel.onSearchButtonClicked(query)

                Toast.makeText(this@ImageQueryFragment.context, "Seach: $query", Toast.LENGTH_SHORT)
                    .show()
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })

    }

    private fun isItFirstLaunchCheck(savedInstanceState: Bundle?) {
        isItTheFirstLaunch = savedInstanceState?.getBoolean(FIRST_LAUNCH) ?: true
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState?.putBoolean(FIRST_LAUNCH, false)
    }
}
