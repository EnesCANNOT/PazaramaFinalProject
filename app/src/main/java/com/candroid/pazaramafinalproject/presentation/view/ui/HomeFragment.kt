package com.candroid.pazaramafinalproject.presentation.view.ui

import PokemonAdapter
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.candroid.pazaramafinalproject.presentation.viewmodel.HomeFragmentViewModel
import com.candroid.pazaramafinalproject.databinding.FragmentHomeBinding
import com.candroid.pazaramafinalproject.databinding.PopupMenuBinding
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
        viewModel.getPokemons()
        initView()
        initListener()
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrEmpty()){
                    //viewModel.getPokemonByName(query)
                    viewModel.setSearchQuery(query)
                } else{
                    viewModel.getPokemons()
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrEmpty()){
                    binding.searchView.queryHint = "Eg: Charmander"
                    viewModel.getPokemons()
                } else {
                    binding.searchView.queryHint = null
                    //viewModel.getPokemonByName(newText)
                    viewModel.setSearchQuery(newText)
                }

                return true
            }
        })


        binding.sortButton.setOnClickListener {
            showCustomPopup(requireContext(), it, viewModel)
        }

        viewModel.sortOption.observe(viewLifecycleOwner, Observer {
            when (it.option){
                "Number" -> {
                    pokemonAdapter.updatePokedexList(viewModel.pokemonList.value!!.sortedBy { it.number })
                }
                "Name" -> {
                    pokemonAdapter.updatePokedexList(viewModel.pokemonList.value!!.sortedBy { it.pokemonName })
                }
            }
        })
    }

    private fun initView() {
        val layoutManager = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
        binding.pokemonRecyclerView.layoutManager = layoutManager
        binding.pokemonRecyclerView.adapter = pokemonAdapter

//        binding.pokemonRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
//            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//                super.onScrolled(recyclerView, dx, dy)
//                val totalItemCount = layoutManager.itemCount
//                val lastVisibleItemPosition = layoutManager.findLastVisibleItemPositions(null).maxOrNull()
//                viewModel.searchQuery.observe(viewLifecycleOwner, Observer{
//                    if (it.isNullOrEmpty()){
//                        if (lastVisibleItemPosition != null && lastVisibleItemPosition == totalItemCount - 1) {
//                            // Load more Pokémon when the user reaches the end of the list
//                            viewModel.getPokemons()
//                        }
//                    }
//                })
//            }
//        })

        binding.pokemonRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val totalItemCount = layoutManager.itemCount
                val lastVisibleItemPosition = layoutManager.findLastVisibleItemPositions(null).maxOrNull()
                if (lastVisibleItemPosition != null && lastVisibleItemPosition == totalItemCount - 1) {
                    // Load more Pokémon when the user reaches the end of the list
                    if (viewModel.searchQuery.value.isNullOrEmpty()) {
                        // Load more Pokémon when the user reaches the end of the list
                        viewModel.getPokemons()
                    } else {
                        // For search queries, load more Pokémon based on the search query
                        viewModel.searchQuery.value?.let { searchQuery ->
                            viewModel.getPokemonByName(searchQuery)
                        }
                    }
                }
            }
        })

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