package com.candroid.pazaramafinalproject.presentation.view.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.candroid.pazaramafinalproject.presentation.viewmodel.HomeFragmentViewModel
import com.candroid.pazaramafinalproject.databinding.FragmentHomeBinding
import com.candroid.pazaramafinalproject.databinding.PopupMenuBinding
import com.candroid.pazaramafinalproject.presentation.view.adapter.PokemonAdapter
import com.candroid.pazaramafinalproject.util.showCustomPopup

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var popupMenuBinding: PopupMenuBinding
    private val viewModel: HomeFragmentViewModel by viewModels(ownerProducer = {this})
    private var pokemonAdapter: PokemonAdapter = PokemonAdapter()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        popupMenuBinding = PopupMenuBinding.inflate(layoutInflater, null, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.loadPokemon()
        initView()
        initListener()
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    viewModel.searchQuery.value = it
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrEmpty()){
                    binding.searchView.queryHint = "Eg: Charmander"
                } else {
                    newText.let {
                        viewModel.searchQuery.value = it
                    }
                    binding.searchView.queryHint = null
                }

                return true
            }
        })


        binding.sortButton.setOnClickListener {
            showCustomPopup(requireContext(), it, viewModel)
        }
    }

    private fun initView() {
        val layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        binding.pokemonRecyclerView.layoutManager = layoutManager
        binding.pokemonRecyclerView.adapter = pokemonAdapter
    }

    private fun initListener() {
        viewModel.pokemonList.observe(viewLifecycleOwner) {
            pokemonAdapter.updatePokedexList(it)
        }

        /*
        viewModel.isLoading.observe(viewLifecycleOwner) {
            it?.let {
                if (it){
                    binding.homeScreenProgressBar.visibility = View.VISIBLE
                    binding.homeScreenFragmentRV.visibility = View.GONE
                    binding.homeScreenError.visibility = View.GONE
                } else {
                    binding.homeScreenFragmentRV.visibility = View.VISIBLE
                    binding.homeScreenProgressBar.visibility = View.GONE
                }
            }
        }

        viewModel.error.observe(viewLifecycleOwner) {
            it?.let {
                binding.homeScreenError.visibility = View.VISIBLE
                binding.homeScreenError.text = it
            }
        }
    */
    }
}