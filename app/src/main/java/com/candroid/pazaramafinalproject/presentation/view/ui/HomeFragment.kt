package com.candroid.pazaramafinalproject.presentation.view.ui

import PokemonAdapter
import android.os.Bundle
import android.util.Log
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
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var popupMenuBinding: PopupMenuBinding
    private val viewModel: HomeFragmentViewModel by viewModels(ownerProducer = {this})
    private val pokemonAdapter: PokemonAdapter by lazy {
        PokemonAdapter()
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        popupMenuBinding = PopupMenuBinding.inflate(layoutInflater, null, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.fetchPokemonList()
        initView()
        initListener()

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query.isNullOrEmpty()){
                    viewModel.apply { setFilteredList().also { setSearchQueryText("") } }
                    viewModel.fetchPokemonList()
                    pokemonAdapter.updatePokedexList(viewModel.pokemonList.value!!)
                } else{
                    viewModel.getPokemonByName(query)
                    viewModel.filteredList.value?.let { pokemonAdapter.updatePokedexList(it) }
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrEmpty()){
                    binding.searchView.queryHint = "Eg: Charmander or #0004"
                    viewModel.apply { setFilteredList().also { setSearchQueryText("") } }
                    viewModel.fetchPokemonList()
                    pokemonAdapter.updatePokedexList(viewModel.pokemonList.value!!)
                } else {
                    binding.searchView.queryHint = null
                    viewModel.setSearchQueryText(newText)
                    viewModel.filteredList.value?.let { pokemonAdapter.updatePokedexList(it) }
                    viewModel.getPokemonByName(newText)
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
                    viewModel.pokemonList.value?.let { it1 -> pokemonAdapter.updatePokedexList(it1.sortedBy { it.number }) }
                }
                "Name" -> {
                    pokemonAdapter.updatePokedexList(viewModel.pokemonList.value!!.sortedBy { it.pokemonName })
                }
            }
        })

    }

    private fun initView(){
        val layoutManager = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
        binding.pokemonRecyclerView.layoutManager = layoutManager
        binding.pokemonRecyclerView.adapter = pokemonAdapter

        binding.pokemonRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val totalItemCount = layoutManager.itemCount
                val lastVisibleItemPosition = layoutManager.findLastVisibleItemPositions(null).maxOrNull()
                if (lastVisibleItemPosition != null && lastVisibleItemPosition == totalItemCount - 1) {
                    if (viewModel.searchQuery.value.isNullOrEmpty()) {
                        viewModel.fetchPokemonList()
                        viewModel.pokemonList.value?.let {
                            pokemonAdapter.updatePokedexList(it)
                        }
                    }
                    else {
                        viewModel.searchQuery.value?.let { searchQuery ->
                            viewModel.getPokemonByName(searchQuery)
                            viewModel.filteredList.value?.let {
                                pokemonAdapter.updatePokedexList(it)
                                Log.i("Hebele", "search query changed")
                            }
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