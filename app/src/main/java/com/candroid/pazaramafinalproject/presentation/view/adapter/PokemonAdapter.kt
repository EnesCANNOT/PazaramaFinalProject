package com.candroid.pazaramafinalproject.presentation.view.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.candroid.pazaramafinalproject.data.models.PokedexListEntry
import com.candroid.pazaramafinalproject.databinding.PokemonItemBinding
import com.candroid.pazaramafinalproject.util.downloadUrl
import com.candroid.pazaramafinalproject.util.placeHolderProgressBar

class PokemonAdapter : RecyclerView.Adapter<PokemonAdapter.PokemonHolder>() {

    inner class PokemonHolder(val binding: PokemonItemBinding) : RecyclerView.ViewHolder(binding.root)

    private var pokedexList: List<PokedexListEntry> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonHolder {
        val binding = PokemonItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PokemonHolder(binding)
    }

    override fun getItemCount(): Int = pokedexList.size

    override fun onBindViewHolder(holder: PokemonHolder, position: Int) {
        val pokedex = pokedexList[position]
        holder.binding.pokemonId.text = pokedex.number.toString()
        holder.binding.pokemonName.text = pokedex.pokemonName
        holder.binding.pokemonImageView.downloadUrl(pokedex.imageUrl, placeHolderProgressBar(holder.binding.root.context))
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updatePokedexList(pokedexes: List<PokedexListEntry>){
        pokedexList = pokedexes
        notifyDataSetChanged()
    }
}