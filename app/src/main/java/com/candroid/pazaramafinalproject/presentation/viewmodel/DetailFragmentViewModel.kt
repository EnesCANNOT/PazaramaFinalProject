package com.candroid.pazaramafinalproject.presentation.viewmodel

import android.app.Activity
import android.content.res.ColorStateList
import android.os.Build
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.Glide
import com.candroid.pazaramafinalproject.R
import com.candroid.pazaramafinalproject.data.remote.responses.Pokemon
import com.candroid.pazaramafinalproject.data.remote.responses.PokemonSpecies
import com.candroid.pazaramafinalproject.data.remote.responses.Type
import com.candroid.pazaramafinalproject.data.repository.PokemonRepository
import com.candroid.pazaramafinalproject.databinding.FragmentDetailBinding
import com.candroid.pazaramafinalproject.util.Resource
import com.candroid.pazaramafinalproject.util.UtilsActivity
import com.candroid.pazaramafinalproject.util.downloadUrl
import com.candroid.pazaramafinalproject.util.placeHolderProgressBar
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class DetailFragmentViewModel @Inject constructor(val pokemonRepository: PokemonRepository) :
    ViewModel() {

    private val _selectedId = MutableLiveData<Int>(1)
    val selectedId: LiveData<Int> = _selectedId

    private val _selectedPokemon = MutableLiveData<Pokemon?>()
    val selectedPokemon: LiveData<Pokemon?> = _selectedPokemon

    private val _selectedPokemonSpecies = MutableLiveData<PokemonSpecies?>()
    val selectedPokemonSpecies: LiveData<PokemonSpecies?> = _selectedPokemonSpecies

    fun setSelectedPokemon(binding: FragmentDetailBinding, selectedId: Int) {
        _selectedId.value = selectedId
        setNavigationButtons(binding)
        getSelectedPokemon()
        //displayPokemon(binding)
    }

    private fun getSelectedPokemon() {
        viewModelScope.launch {
            if (_selectedId.value == null) {
                Log.i("HebelePokemon", "selectedId.value is null")
            } else {
                var result: Resource<Pokemon>
                runBlocking {
                    result = pokemonRepository.getPokemonById(_selectedId.value!!)
                    _selectedPokemonSpecies.value =
                        pokemonRepository.getPokemonSpeciesById(_selectedId.value!!).data
                }
                when (result) {
                    is Resource.Success -> {
                        _selectedPokemon.value = result.data
                    }

                    is Resource.Error -> {
                        _selectedPokemon.value = result.data
                    }

                    is Resource.Loading -> {
                        Log.i("HebelePokemon", "Loading")
                    }
                }
            }
        }
    }

    private fun setNavigationButtons(binding: FragmentDetailBinding) {
        binding.navigateBeforeIV.visibility =
            if (selectedId.value == 1) View.INVISIBLE else View.VISIBLE
        binding.navigateNextIV.visibility =
            if (selectedId.value == 10275) View.INVISIBLE else View.VISIBLE
    }

    fun displayPokemon(binding: FragmentDetailBinding, pokemon: Pokemon) {
        setBackgroundColors(binding, pokemon.types.first().type.name)
        val imageUrl =
            "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/${pokemon.id}.png"
        binding.selectedPokemonIV.downloadUrl(
            imageUrl,
            placeHolderProgressBar(binding.root.context)
        )
        binding.selectedPokemonNameTV.text = pokemon.name.capitalize(Locale.ROOT)
        binding.weightAmountTV.text = "${pokemon.weight / 10.0} kg".replace(".", ",")
        binding.heightAmountTV.text = "${pokemon.height / 10.0} m".replace(".", ",")
        binding.selectedPokemonNumberTV.text = String.format("#%05d", pokemon.id)
        binding.firstAbilityTV.text = "${pokemon.moves[0].move.name}"
        binding.secondAbilityTV.text = "${pokemon.moves[1].move.name}"
        setChipsVisibility(binding, pokemon.types)
        _selectedPokemonSpecies.value?.let {
            binding.pokemonDescriptionTV.text =
                it.flavor_text_entries[9].flavor_text.replace("\n", " ")
        }
        setStats(binding, pokemon)
    }

    private fun setChipsVisibility(binding: FragmentDetailBinding, types: List<Type>) {
        val chipMap = mapOf(
            "bug" to binding.chipBugstatus,
            "dark" to binding.chipDarkStatus,
            "dragon" to binding.chipDragonStatus,
            "electric" to binding.chipElectricStatus,
            "fairy" to binding.chipFairyStatus,
            "fighting" to binding.chipFightingStatus,
            "fire" to binding.chipFireStatus,
            "flying" to binding.chipFlyingStatus,
            "ghost" to binding.chipGhostStatus,
            "grass" to binding.chipGrassStatus,
            "ground" to binding.chipGroundStatus,
            "ice" to binding.chipIceStatus,
            "normal" to binding.chipNormalStatus,
            "poison" to binding.chipPoisonStatus,
            "psychic" to binding.chipPsychicStatus,
            "rock" to binding.chipRockStatus,
            "steel" to binding.chipSteelStatus,
            "type" to binding.chipTypeStatus,
            "water" to binding.chipWaterStatus
        )

        for (chip in chipMap.values) {
            chip.visibility = View.GONE
        }

        for (type in types.map { it.type.name }) {
            val chip = chipMap[type]
            chip?.visibility = View.VISIBLE
        }
    }

    private fun setBackgroundColors(binding: FragmentDetailBinding, type: String) {
        val backgroundColor = when (type) {
            "bug" -> R.color.bug
            "dark" -> R.color.dark
            "dragon" -> R.color.dragon
            "electric" -> R.color.electric
            "fairy" -> R.color.fairy
            "fighting" -> R.color.fighting
            "fire" -> R.color.fire
            "flying" -> R.color.flying
            "ghost" -> R.color.ghost
            "grass" -> R.color.grass
            "ground" -> R.color.ground
            "ice" -> R.color.ice
            "normal" -> R.color.normal
            "poison" -> R.color.poison
            "psychic" -> R.color.psychic
            "rock" -> R.color.rock
            "steel" -> R.color.steel
            "type" -> R.color.type
            "water" -> R.color.water
            else -> R.color.primary
        }

        val activity = UtilsActivity.getCurrentActivity()
        activity?.let {
            val window: Window = activity.window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = ContextCompat.getColor(activity, backgroundColor)
            window.navigationBarColor = ContextCompat.getColor(activity, backgroundColor)
        }
        binding.root.setBackgroundColor(
            ContextCompat.getColor(
                binding.root.context,
                backgroundColor
            )
        )
        binding.aboutTV.setTextColor(ContextCompat.getColor(binding.root.context, backgroundColor))
        binding.baseStatsTV.setTextColor(
            ContextCompat.getColor(
                binding.root.context,
                backgroundColor
            )
        )
        binding.hpBaseStatePB.progressTintList =
            ColorStateList.valueOf(ContextCompat.getColor(binding.root.context, backgroundColor))
        binding.atkBaseStatePB.progressTintList =
            ColorStateList.valueOf(ContextCompat.getColor(binding.root.context, backgroundColor))
        binding.defBaseStatePB.progressTintList =
            ColorStateList.valueOf(ContextCompat.getColor(binding.root.context, backgroundColor))
        binding.satkBaseStatePB.progressTintList =
            ColorStateList.valueOf(ContextCompat.getColor(binding.root.context, backgroundColor))
        binding.sdefBaseStatePB.progressTintList =
            ColorStateList.valueOf(ContextCompat.getColor(binding.root.context, backgroundColor))
        binding.spdBaseStatePB.progressTintList =
            ColorStateList.valueOf(ContextCompat.getColor(binding.root.context, backgroundColor))
    }

    private fun setStats(binding: FragmentDetailBinding, pokemon: Pokemon){
        val statsProgressBars = listOf(
            binding.hpBaseStatePB,
            binding.atkBaseStatePB,
            binding.defBaseStatePB,
            binding.satkBaseStatePB,
            binding.sdefBaseStatePB,
            binding.spdBaseStatePB
        )

        val statsTextViews = listOf(
            binding.hpBaseStateTV,
            binding.atkBaseStateTV,
            binding.defBaseStateTV,
            binding.satkBaseStateTV,
            binding.sdefBaseStateTV,
            binding.spdBaseStateTV
        )

        val baseStats = statsTextViews.zip(statsProgressBars)

        (0..5).forEach {
            baseStats[it].first.text = String.format("%03d", pokemon.stats[it].base_stat)
            baseStats[it].second.progress = pokemon.stats[it].base_stat
        }
    }
}