package com.redgunner.droidsoft.view.fragment

import android.util.Log
import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.google.android.material.tabs.TabLayout
import com.redgunner.droidsoft.R
import com.redgunner.droidsoft.adapter.PostListAdapter
import com.redgunner.droidsoft.adapter.PostLoadStateAdapter
import com.redgunner.droidsoft.models.category.Categories
import com.redgunner.droidsoft.viewmodel.SharedViewModel
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.GestureDetector
import android.view.MotionEvent
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatDelegate


class MainFragment : Fragment(R.layout.fragment_main) {

    private val viewModel: SharedViewModel by activityViewModels()
    private val postAdapter = PostListAdapter { postId ->

        findNavController().navigate(MainFragmentDirections.actionGlobalDetailFragment(postId))


    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpRecyclerView()

        tablayout_shimmer_view_container.startShimmer()

        lifecycleScope.launch {


            postAdapter.loadStateFlow.map {
                it.refresh

            }
                .distinctUntilChanged()
                .collect { LoadState ->

                    when (LoadState) {
                        is LoadState.Loading -> {


                            HomePostList.isVisible = false
                            list_shimmer_view_container.isVisible = true


                            list_shimmer_view_container.startShimmer()

                        }
                        is LoadState.NotLoading -> {

                            list_shimmer_view_container.stopShimmer()
                            list_shimmer_view_container.isVisible = false
                            HomePostList.isVisible = true


                        }
                        is LoadState.Error -> {


                        }
                    }

                }
        }


        viewModel.posts.observe(viewLifecycleOwner, { pagingData ->

            postAdapter.submitData(lifecycle = lifecycle, pagingData = pagingData)

        })

        val switchThm = view.findViewById<ImageButton>(R.id.themeSwitch)
        switchThm.setOnClickListener {
            if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

            }
            else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)

            }
        }
        lifecycleScope.launchWhenStarted {


            viewModel.categories.collect { categories ->

                if (categories.isNotEmpty()) {
                    tablayout_shimmer_view_container.stopShimmer()

                    tablayout_shimmer_view_container.isVisible = false
                    tabLayout.isVisible = true

                    setUpCategoriesTabLayout(categories)
                }

            }
        }

    }

    override fun onStart() {
        super.onStart()

        tabLayout.setScrollPosition(viewModel.tabLayoutPosition, 0f, false)

    }


    override fun onResume() {
        super.onResume()
        tabLayout.getTabAt(viewModel.tabLayoutPosition)?.select()
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {


            override fun onTabSelected(tab: TabLayout.Tab?) {


                HomePostList.scrollToPosition(0)
                viewModel.getPostByCategory(tab!!.position)
                viewModel.saveTabLayoutPosition(tab.position)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

        })
    }

    private fun setUpRecyclerView() {
        HomePostList.apply {

            this.adapter = postAdapter.withLoadStateFooter(PostLoadStateAdapter())
        }
    }

    private fun setUpCategoriesTabLayout(categories: List<Categories>) {

        tabLayout.addTab(tabLayout.newTab().setText("Dernières News"))

        for (category in categories) {
            val tab = tabLayout.newTab()
            tab.text = category.name
            when (tab.text) {
                "Actualité" -> {
                    tab.setIcon(R.drawable.ic_baseline_newspaper_24)
                }
                "Dossier" -> {
                    tab.setIcon(R.drawable.ic_baseline_library_books_24)
                }
                "Tests Android" -> {
                    tab.setIcon(R.drawable.ic_baseline_smartphone_24)
                }
            }

            val colorStateList = ColorStateList(
                arrayOf(intArrayOf(android.R.attr.state_selected), // selected
                    intArrayOf(-android.R.attr.state_selected) // unselected
                ),
                intArrayOf(
                    Color.BLACK, // selected
                    Color.parseColor("#30ae6e") // unselected
                )
            )
            tabLayout.setTabIconTint(colorStateList)

            tabLayout.addTab(tab)
        }
    }
}