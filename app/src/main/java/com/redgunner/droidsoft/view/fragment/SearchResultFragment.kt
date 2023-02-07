package com.redgunner.droidsoft.view.fragment

import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListAdapter
import android.widget.ListView
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.redgunner.droidsoft.R
import com.redgunner.droidsoft.viewmodel.SharedViewModel
import com.redgunner.droidsoft.models.post.Post
import kotlinx.android.synthetic.main.fragment_search.*
import androidx.appcompat.app.AppCompatActivity
import java.util.Arrays
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_search_result.*

class SearchResultFragment: Fragment(R.layout.fragment_search_result) {
    private val viewModel: SharedViewModel by activityViewModels()

    private val shimmer = list_shimmer_view_container
    private val postList = HomePostList

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        searchBack.setOnClickListener(){
            findNavController().popBackStack()
        }
    }
}