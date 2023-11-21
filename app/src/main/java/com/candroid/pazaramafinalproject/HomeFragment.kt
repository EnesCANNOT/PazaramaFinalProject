package com.candroid.pazaramafinalproject

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.candroid.pazaramafinalproject.databinding.FragmentHomeBinding
import com.candroid.pazaramafinalproject.databinding.PopupMenuBinding
import com.candroid.pazaramafinalproject.util.SortOption
import com.candroid.pazaramafinalproject.util.SortOption.*
import com.candroid.pazaramafinalproject.util.showCustomPopup

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var popupMenuBinding: PopupMenuBinding
    private lateinit var viewModel: HomeFragmentViewModel
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        popupMenuBinding = PopupMenuBinding.inflate(layoutInflater, null, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(HomeFragmentViewModel::class.java)

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    viewModel.searchQuery.value = it
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {
                    viewModel.searchQuery.value = it
                }
                return true
            }
        })


        binding.sortButton.setOnClickListener {
            showCustomPopup(requireContext(), it, viewModel)
        }
    }
}