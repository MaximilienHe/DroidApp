package com.redgunner.droidsoft.view.fragment

import android.content.res.ColorStateList
import android.graphics.Color
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
import androidx.core.content.res.ResourcesCompat
import com.facebook.shimmer.ShimmerFrameLayout
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.paging.LoadState
import com.redgunner.droidsoft.adapter.PostListAdapter
import com.redgunner.droidsoft.adapter.PostLoadStateAdapter
import com.redgunner.droidsoft.models.category.Categories
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.fragment_search_result.*
import kotlinx.android.synthetic.main.fragment_search_result.HomePostList2
import kotlinx.android.synthetic.main.fragment_search_result.list_shimmer_view_container2
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class SearchResultFragment: Fragment(R.layout.fragment_search_result) {
    private val viewModel: SharedViewModel by activityViewModels()
    private val shimmer = list_shimmer_view_container2
    private val postList = HomePostList2
    private val navArgs: SearchResultFragmentArgs by navArgs()
    private val postAdapter = PostListAdapter { postId ->
        findNavController().navigate(SearchResultFragmentDirections.actionGlobalSearchResultFragmentToDetailFragment(postId))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpRecyclerView()

        shimmer.startShimmer()

        lifecycleScope.launch {
            postAdapter.loadStateFlow.map {
                it.refresh
            }
                .distinctUntilChanged()
                .collect { LoadState ->
                    when (LoadState) {
                        is LoadState.Loading -> {
                            postList.isVisible = false
                            shimmer.isVisible = true
                            shimmer.startShimmer()
                        }
                        is LoadState.NotLoading -> {
                            shimmer.stopShimmer()
                            shimmer.isVisible = false
                            postList.isVisible = true
                        }
                        is LoadState.Error -> {
                            Log.d("SearchResultLoadError", "error loading")

                        }
                    }
                }
        }


        viewModel.posts.observe(viewLifecycleOwner, { pagingData ->
            postAdapter.submitData(lifecycle = lifecycle, pagingData = pagingData)
        })


        searchBack.setOnClickListener(){
            findNavController().popBackStack()
        }
    }

    override fun onResume() {
        super.onResume()
        postList.scrollToPosition(0)
        viewModel.getSearchResult(navArgs!!.title)
    }
    private fun setUpRecyclerView() {
        postList.apply {
            this.adapter = postAdapter.withLoadStateFooter(PostLoadStateAdapter())
        }
    }


}