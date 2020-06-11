package com.example.imagesearch.ui.image.query

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.imagesearch.R

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
    }

}
