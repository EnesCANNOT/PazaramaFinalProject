package com.candroid.pazaramafinalproject.presentation.view.ui

import PokemonAdapter
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.candroid.pazaramafinalproject.R
import com.candroid.pazaramafinalproject.presentation.viewmodel.HomeFragmentViewModel
import com.candroid.pazaramafinalproject.databinding.FragmentHomeBinding
import com.candroid.pazaramafinalproject.databinding.PopupMenuBinding
import com.candroid.pazaramafinalproject.util.UtilsActivity
import com.candroid.pazaramafinalproject.util.showCustomPopup
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

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
                    viewModel.getPokemon(query)
                    //viewModel.filteredList.value?.let { pokemonAdapter.updatePokedexList(it) }
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrEmpty()){
                    binding.searchView.queryHint = "Eg: Pikachu or #00025"
                    viewModel.apply { setFilteredList().also { setSearchQueryText("") } }
                    viewModel.fetchPokemonList()
                    //pokemonAdapter.updatePokedexList(viewModel.pokemonList.value!!)
                    return true
                } else {
                    binding.searchView.queryHint = null
                    viewModel.getPokemon(newText)
//                    if(viewModel.filteredList.value != null){
//                        pokemonAdapter.updatePokedexList(viewModel.filteredList.value!!)
//                    }  else{
//                        pokemonAdapter.updatePokedexList(viewModel.pokemonList.value!!)
//                    }
//                    viewModel.filteredList.value?.let { pokemonAdapter.updatePokedexList(it) }
                    return true
                }
            }
        })

        viewModel.filteredList.observe(viewLifecycleOwner, Observer { filteredList ->
            filteredList?.let { pokemonAdapter.updatePokedexList(it) }
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
//                    else {
//                        viewModel.searchQuery.value?.let { searchQuery ->
//                            viewModel.getPokemon(searchQuery)
//                            viewModel.filteredList.value?.let {
//                                pokemonAdapter.updatePokedexList(it)
//                                Log.i("Hebele", "search query changed")
//                            }
//                        }
//                    }
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

    override fun onResume() {
        super.onResume()

        viewModel.sortList()

        viewModel.setSearchQueryText("")
        binding.searchView.setQuery("", true)

        val activity = UtilsActivity.getCurrentActivity()
        activity?.let {
            val window: Window = activity.window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = ContextCompat.getColor(activity, R.color.primary)
            window.navigationBarColor = ContextCompat.getColor(activity, R.color.primary)
        }
    }

}