package com.candroid.pazaramafinalproject.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.candroid.pazaramafinalproject.data.models.PokedexListEntry
import com.candroid.pazaramafinalproject.data.remote.responses.Pokemon
import com.candroid.pazaramafinalproject.data.repository.PokemonRepository
import com.candroid.pazaramafinalproject.util.Constants.PAGE_SIZE
import com.candroid.pazaramafinalproject.util.Resource
import com.candroid.pazaramafinalproject.util.SortOption
import com.candroid.pazaramafinalproject.util.SortOptionDrawable
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class HomeFragmentViewModel @Inject constructor(val pokemonRepository: PokemonRepository) :
    ViewModel() {

    //private var loadingJob: Job? = null
    private var currentPage = 0

    val sortOption = MutableLiveData<SortOption>(SortOption.NUMBER)
    val sortOptionDrawable = MutableLiveData<SortOptionDrawable>(SortOptionDrawable.NUMBER)

    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String>("")
    val error: LiveData<String> = _error

    private val _searchQuery = MutableLiveData<String>("")
    val searchQuery: LiveData<String> = _searchQuery

    private val _pokemonList = MutableLiveData<List<PokedexListEntry>>(listOf())
    val pokemonList: LiveData<List<PokedexListEntry>> = _pokemonList

    private val _filteredList = MutableLiveData<List<PokedexListEntry>>(listOf())
    val filteredList: LiveData<List<PokedexListEntry>> = _filteredList
    fun fetchPokemonList() {
        viewModelScope.launch {
            if (isLoading.value == true) return@launch
            _isLoading.value = true

            val result = pokemonRepository.getPokemonList(PAGE_SIZE, currentPage * PAGE_SIZE)
            when (result) {
                is Resource.Success -> {
                    _isLoading.value = false
                    val pokedexEntries = result.data?.results?.mapIndexed { index, entry ->
                        val number = if (entry.url.endsWith("/")){
                            entry.url.dropLast(1).takeLastWhile { it.isDigit() }
                        } else {
                            entry.url.takeLastWhile { it.isDigit() }
                        }
                        val imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/${number}.png"
                        PokedexListEntry(entry.name.capitalize(Locale.ROOT), imageUrl, number.toInt())
                    }
                    _pokemonList.value = _pokemonList.value?.plus(pokedexEntries ?: listOf())
                    currentPage++
                }

                is Resource.Error -> {
                    _error.value = result.message.toString()
                    _isLoading.value = false
                }

                is Resource.Loading -> {
                    _isLoading.value = true
                }
            }
        }
    }

    fun getPokemonByName(name: String){
        _searchQuery.value = name
        viewModelScope.launch {
            val result = pokemonRepository.getPokemonByName(name)
            when(result) {
                is Resource.Success -> {
                    val pokemon = result.data
                    pokemon?.let {
                        val imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/${it.id}.png"
                        _filteredList.value = listOf(PokedexListEntry(it.name, imageUrl, it.id))
                    }
                }

                is Resource.Error -> {
                    _error.value = result.message.toString()
                    _isLoading.value = false
                }

                is Resource.Loading -> {
                    _isLoading.value = true
                }
            }
        }
    }

    fun setFilteredList(){
        _filteredList.value = listOf()
    }

    fun setSearchQueryText(searchQuery: String){
        _searchQuery.value = searchQuery
    }

//
//    fun sortList(option: SortOption){
//        when (sortOption.value!!.option){
//            "Number" -> {
//                _pokemonList.value?.sortedBy { it.number }
//            }
//            "Name" -> {
//                _pokemonList.value?.sortedBy { it.pokemonName }
//            }
//        }
//    }
}