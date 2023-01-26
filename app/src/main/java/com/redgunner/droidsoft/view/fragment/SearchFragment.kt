package com.redgunner.droidsoft.view.fragment

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.redgunner.droidsoft.R
import kotlinx.android.synthetic.main.fragment_search.*

class SearchFragment : Fragment(R.layout.fragment_search) {

    override fun onResume() {
        super.onResume()

        searchBack.setOnClickListener {
            findNavController().popBackStack()

        }
    }


}