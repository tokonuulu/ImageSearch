package com.example.imagesearch.ui.image.query

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.provider.SearchRecentSuggestions
import android.view.*
import android.widget.SearchView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.imagesearch.R
import com.example.imagesearch.data.db.entity.ImageDescription
import com.example.imagesearch.internals.QueryState
import com.example.imagesearch.ui.MainActivity
import com.example.imagesearch.ui.base.ScopedFragment
import kotlinx.android.synthetic.main.image_query_fragment.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

class ImageQueryFragment : ScopedFragment(), KodeinAware, OnItemClickListener {

    override val kodein by closestKodein()
    private val viewModelFactory: ImageQueryViewModelFactory by instance()

    private lateinit var imageViewModel: ImageQueryViewModel

    private lateinit var imageAdapter: ImageListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        setHasOptionsMenu(true)

        imageAdapter = ImageListAdapter(this)

        return inflater.inflate(R.layout.image_query_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        imageViewModel = ViewModelProvider((this.activity as MainActivity), viewModelFactory)
            .get(ImageQueryViewModel::class.java)

        initRecyclerView()

        startObservingQueryResult()
        startObservingQueryState()
    }

    private fun startObservingQueryState() {
        imageViewModel.queryState.observe(viewLifecycleOwner, Observer { queryState ->

            when (queryState) {
                QueryState.RUNNING -> group_loading.visibility = View.VISIBLE
                QueryState.SUCCESS -> group_loading.visibility = View.GONE

                QueryState.DB_ERROR -> {
                    Toast.makeText(
                        this.context as MainActivity,
                        "Database error",
                        Toast.LENGTH_SHORT
                    ).show()
                    group_loading.visibility = View.GONE
                    imageViewModel.onErrorHandled()
                }
                QueryState.NETWORK_ERROR -> {
                    Toast.makeText(
                        this.context as MainActivity,
                        "Please check your internet connection",
                        Toast.LENGTH_SHORT
                    ).show()
                    group_loading.visibility = View.GONE
                    imageViewModel.onErrorHandled()
                }
                QueryState.UNKNOWN_ERROR -> {
                    Toast.makeText(
                        this.context as MainActivity,
                        "Unknown error",
                        Toast.LENGTH_SHORT
                    ).show()
                    group_loading.visibility = View.GONE
                    imageViewModel.onErrorHandled()
                }
            }
        })
    }

    private fun startObservingQueryResult() {
        imageViewModel.imageList.observe(viewLifecycleOwner, Observer {
            val newList = mutableListOf<ImageDescription>()
            it.forEach { item -> newList.add(item.copy()) }
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

        inflater.inflate(R.menu.search_menu, menu)

        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView
        val searchManager : SearchManager = this.activity?.getSystemService(Context.SEARCH_SERVICE) as SearchManager

        searchView.setSearchableInfo(searchManager.getSearchableInfo(this.activity?.componentName))
        searchView.queryHint = "Search image..."
        searchView.isIconifiedByDefault = false
        searchView.maxWidth = Int.MAX_VALUE

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {

                // Saving queries
                SearchRecentSuggestions(
                    this@ImageQueryFragment.context,
                    SuggestionProvider.AUTHORITY,
                    SuggestionProvider.MODE
                )
                    .saveRecentQuery(query, null)

                searchView.clearFocus()
                // Notify the ViewModel
                imageViewModel.loadNewImages(query)

                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
    }
}
