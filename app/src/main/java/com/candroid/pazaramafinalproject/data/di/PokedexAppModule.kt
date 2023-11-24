package com.candroid.pazaramafinalproject.data.di

import com.candroid.pazaramafinalproject.data.remote.service.PokemonApi
import com.candroid.pazaramafinalproject.data.repository.PokemonRepository
import com.candroid.pazaramafinalproject.util.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PokedexAppModule {
    @Singleton
    @Provides
    fun retrofitInstance(): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Singleton
    @Provides
    fun injectPokemonApi(retrofitInstance: Retrofit): PokemonApi =
        retrofitInstance.create(PokemonApi::class.java)

    @Singleton
    @Provides
    fun injectPokemonRepository(): PokemonRepository = PokemonRepository(
        injectPokemonApi(
            retrofitInstance()
        )
    )
}