package com.redgunner.droidsoft.view.fragment

import android.os.Bundle
import android.text.Html
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
import kotlinx.android.synthetic.main.fragment_search.*
import androidx.appcompat.app.AppCompatActivity
import java.util.Arrays
import android.widget.Toast

class SearchFragment : Fragment(R.layout.fragment_search) {


    private lateinit var searchView: SearchView
    private lateinit var listView: ListView
    private lateinit var list: ArrayList<String>
    private lateinit var adapter: ArrayAdapter<*>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchView = searchBar
        listView = pre_result

        list = ArrayList()
        list.add("Samsung")
        list.add("Huawei")
        list.add("One plus")
        list.add("la grosse daronne à maximilien cte chienne")

        adapter = ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_expandable_list_item_1,
            list
        )
        listView.adapter = adapter

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                if (list.contains(query)) {
                    adapter.filter.filter(query)
                } else {
                    Toast.makeText(requireContext(), "No Match found", Toast.LENGTH_LONG).show()
                }
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                adapter.filter.filter(newText)
                return false
            }
        })
    }
}