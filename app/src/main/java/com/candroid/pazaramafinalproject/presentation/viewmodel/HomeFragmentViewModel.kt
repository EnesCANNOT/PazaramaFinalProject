package com.candroid.pazaramafinalproject.presentation.viewmodel

import android.util.Log
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Locale

class HomeFragmentViewModel : ViewModel() {
    private val repository = PokemonRepository()

    private var currentPage = 0

    val sortOption = MutableLiveData<SortOption>(SortOption.NUMBER)
    val sortOptionDrawable = MutableLiveData<SortOptionDrawable>(SortOptionDrawable.NUMBER)
    val searchQuery = MutableLiveData<String>("")

    val pokemonList = MutableLiveData<List<PokedexListEntry>>(listOf())

    fun loadPokemon(){
        viewModelScope.launch {
            val result = repository.getPokemonList(PAGE_SIZE, currentPage * PAGE_SIZE)
            when(result) {
                is Resource.Success -> {
                    val pokedexEntries = result.data?.results!!.mapIndexed { index, entry ->
                        val number = if (entry.url.endsWith("/")){
                            entry.url.dropLast(1).takeLastWhile { it.isDigit() }
                        } else {
                            entry.url.takeLastWhile { it.isDigit() }
                        }
                        val url = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/${number}.png"
                        PokedexListEntry(entry.name.capitalize(Locale.ROOT), url, number.toInt())
                    }
                    pokemonList.value = pokemonList.value?.plus(pokedexEntries ?: listOf())
                    pokemonList.value?.forEach {
                        Log.i("Hebele", "${it.number}")
                        Log.i("Hebele", "${it.pokemonName}")
                        Log.i("Hebele", "${it.imageUrl}")
                        getPokemonByName(it.pokemonName.toLowerCase())
                    }
                    currentPage++
                }

                is Resource.Error -> {
                    Log.i("Hebele", "Error scope")
                }

                else -> {
                    Log.i("Hebele", "Else scope")
                }
            }
        }
    }

    fun getPokemonByName(name: String) {
        viewModelScope.launch {
            val result = repository.getPokemonByName(name)
            Log.i("HebeleViewModel", result.message.toString())
            when(result){
                is Resource.Success -> {
                    val x = result.data
                    displayPokemonInfos(x!!)
                }

                is Resource.Error -> {
                    Log.i("Hebele Info", "Error scope : ${result}")
                }

                else -> {
                    Log.i("Hebele Info", "Else scope")
                }
            }
        }
    }

    fun displayPokemonInfos(pokemon: Pokemon){
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