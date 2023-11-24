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
import kotlinx.coroutines.launch
import java.util.Locale

class HomeFragmentViewModel : ViewModel() {
    private val repository = PokemonRepository()

    private var currentPage = 0

    val sortOption = MutableLiveData<SortOption>(SortOption.NUMBER)
    val sortOptionDrawable = MutableLiveData<SortOptionDrawable>(SortOptionDrawable.NUMBER)

    private val _searchQuery = MutableLiveData<String>("")
    val searchQuery: LiveData<String> = _searchQuery

    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error


    val pokemonList = MutableLiveData<List<PokedexListEntry>>(listOf())
    //private val cachedList = mutableListOf<PokedexListEntry>()

    fun getPokemons(){
        viewModelScope.launch {
            if (_isLoading.value == true) return@launch
            _isLoading.value = true

//            if (_searchQuery.value.isNullOrBlank()) {
//                pokemonList.value = emptyList()
//            }

            val result = repository.getPokemonList(PAGE_SIZE, currentPage * PAGE_SIZE)
            when(result) {
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
                    pokemonList.value = pokemonList.value?.plus(pokedexEntries ?: listOf())
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

    fun getPokemonByName(name: String) {
        _searchQuery.value = name
        viewModelScope.launch {
            val result = repository.getPokemonByName(name)
            when(result){
                is Resource.Success -> {
                    val pokemon = result.data
                    pokemon?.let {
                        val imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/${it.id}.png"
                        pokemonList.value = listOf(PokedexListEntry(it.name, imageUrl, it.id))
                        displayPokemonInfos(it)
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

//    fun filterPokemonList(query: String): List<PokedexListEntry>{
//        if(!query.isNullOrBlank()){
//            return
//        }
//    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
        currentPage = 0 // Reset currentPage when a new search is initiated
        viewModelScope.launch {
            pokemonList.value = emptyList() // Clear the existing list when a new search is initiated
            getPokemonByName(query)
        }
    }

    private fun displayPokemonInfos(pokemon: Pokemon){
        Log.i("Hebele Info", "Pokemon ID : ${pokemon.id}")
        pokemon.types.forEach {
            Log.i("Hebele Info", "Pokemon Types : ${it.type.name}")
        }
        Log.i("Hebele Info", "Pokemon Weight : ${pokemon.weight}")
        Log.i("Hebele Info", "Pokemon Height : ${pokemon.height}")
        pokemon.moves.forEach {
            Log.i("Hebele Info", "Pokemon Moves : ${it.move.name}")
        }
        pokemon.stats.forEach {
            Log.i("Hebele Info", "Pokemon Stat Name : ${it.stat.name}")
            Log.i("Hebele Info", "Pokemon Base Stat : ${it.base_stat}")
        }
    }
}