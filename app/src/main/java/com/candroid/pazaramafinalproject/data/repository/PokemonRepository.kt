package com.candroid.pazaramafinalproject.data.repository

import android.util.Log
import com.candroid.pazaramafinalproject.data.remote.responses.Pokemon
import com.candroid.pazaramafinalproject.data.remote.responses.PokemonList
import com.candroid.pazaramafinalproject.data.remote.service.PokemonApi
import com.candroid.pazaramafinalproject.data.remote.service.RetrofitInstance
import com.candroid.pazaramafinalproject.util.Resource

class PokemonRepository{

    private val pokemonApi = RetrofitInstance.pokemonApi

    suspend fun getPokemonList(limit: Int, offset: Int): Resource<PokemonList> {
        return try {
            val response = pokemonApi.getPokemonList(limit, offset)
            Resource.Success(data = response)
        } catch (e: Exception) {
            Resource.Error(message = e.message.toString(), data = null)
        }


    }

    suspend fun getPokemonByName(name: String): Resource<Pokemon> {
        return try {
            val response = pokemonApi.getPokemonByName(name)
            Log.i("HebeleRepo", "$name")
            Log.i("HebeleRepo", "$response")
            Resource.Success(data = response)
        } catch (e: Exception) {
            Resource.Error(message = e.message.toString(), data = null)
        }
    }
}