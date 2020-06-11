package com.example.imagesearch.ui.image.query

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer

import com.example.imagesearch.R
import com.example.imagesearch.data.db.entity.ImageDescription
import com.example.imagesearch.data.network.ImageApiService
import com.example.imagesearch.data.network.ImageNetworkDataSource
import com.example.imagesearch.data.network.ImageNetworkDataSourceImpl
import com.example.imagesearch.data.network.response.ConnectivityInterceptorImpl
import com.example.imagesearch.ui.base.ScopedFragment
import kotlinx.android.synthetic.main.image_query_fragment.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware

import org.kodein.di.generic.instance
import org.kodein.di.android.x.closestKodein

class ImageQuery : ScopedFragment(), KodeinAware {

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
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(ImageQueryViewModel::class.java)


        bindUI()
        /*
        val apiService = ImageApiService(ConnectivityInterceptorImpl(this.context!!))
        val imageNetworkDataSource = ImageNetworkDataSourceImpl(apiService)

        imageNetworkDataSource.downloadedQueryResponse.observe(viewLifecycleOwner, Observer {
            var resp: String = ""
            for (cur: ImageDescription in it.documents)
                resp = resp + cur.displaySitename + '\n'

            text_view.text = resp
        })

        GlobalScope.launch(Dispatchers.Main) {
            imageNetworkDataSource.fetchQueryResponse("lake")
        }*/

    }

    private fun bindUI() = launch{
        val query = viewModel.queryResponse.await()
        query.observe(viewLifecycleOwner,
            Observer {
                if(it == null)
                    return@Observer
                var resp: String = ""
                for (cur: ImageDescription in it)
                    resp = resp + cur.displaySitename + '\n'

                text_view.text = resp
            })
    }

}
