package com.candroid.pazaramafinalproject.data.remote.service

import com.candroid.pazaramafinalproject.util.Constants.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val pokemonApi: PokemonApi by lazy {
        retrofit.create(PokemonApi::class.java)
    }
}