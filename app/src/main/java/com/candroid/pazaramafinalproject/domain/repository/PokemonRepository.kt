package com.candroid.pazaramafinalproject.domain.repository

import com.candroid.pazaramafinalproject.data.remote.responses.Pokemon
import com.candroid.pazaramafinalproject.data.remote.responses.PokemonList
import com.candroid.pazaramafinalproject.data.remote.responses.PokemonSpecies
import com.candroid.pazaramafinalproject.data.remote.service.PokemonApi
import com.candroid.pazaramafinalproject.util.Resource
import javax.inject.Inject

class PokemonRepository @Inject constructor(val pokemonApi: PokemonApi){


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
            Resource.Success(data = response)
        } catch (e: Exception) {
            Resource.Error(message = e.message.toString(), data = null)
        }
    }

    suspend fun getPokemonById(id: Int): Resource<Pokemon> {
        return try {
            val response = pokemonApi.getPokemonById(id)
            Resource.Success(data = response)
        } catch (e: Exception) {
            Resource.Error(message = e.message.toString(), data = null)
        }
    }

    suspend fun getPokemonSpeciesById(id: Int): Resource<PokemonSpecies> {
        return try {
            val response = pokemonApi.getPokemonSpeciesById(id)
            Resource.Success(data = response)
        } catch (e: Exception) {
            Resource.Error(message = e.message.toString(), data = null)
        }
    }
}