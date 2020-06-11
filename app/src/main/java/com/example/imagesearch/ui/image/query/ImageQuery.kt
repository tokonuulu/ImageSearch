package com.example.imagesearch.ui.image.query

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.imagesearch.R
import com.example.imagesearch.data.ImageApiService
import com.example.imagesearch.data.response.QueryResponse
import kotlinx.android.synthetic.main.image_query_fragment.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ImageQuery : Fragment() {

    companion object {
        fun newInstance() = ImageQuery()
    }

    private lateinit var viewModel: ImageQueryViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.image_query_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ImageQueryViewModel::class.java)
        // TODO: Use the ViewModel

        val apiService = ImageApiService()

        GlobalScope.launch(Dispatchers.Main) {
            val response = apiService.searchImages("lake").await()
            text_view.text = response.documents[0].displaySitename
        }
    }

}
