package com.candroid.pazaramafinalproject.presentation.view.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.candroid.pazaramafinalproject.databinding.FragmentDetailBinding
import com.candroid.pazaramafinalproject.presentation.viewmodel.DetailFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class DetailFragment : Fragment() {
    private lateinit var binding: FragmentDetailBinding
    private val viewModel: DetailFragmentViewModel by viewModels(ownerProducer = {this})
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bundle: DetailFragmentArgs by navArgs()

        viewModel.setSelectedPokemon(binding, bundle.selectedId)

        binding.navigateBeforeIV.setOnClickListener {
            Log.i("HebeleDetailFragment", "Selected Pokemon ID : ${viewModel.selectedId.value}")
            viewModel.setSelectedPokemon(binding, viewModel.selectedId.value!! - 1)
            Log.i("HebeleDetailFragment", "Selected Pokemon ID : ${viewModel.selectedId.value}")
        }

        binding.navigateNextIV.setOnClickListener {
            Log.i("HebeleDetailFragment", "Selected Pokemon ID : ${viewModel.selectedId.value}")
            viewModel.setSelectedPokemon(binding, viewModel.selectedId.value!! + 1)
            Log.i("HebeleDetailFragment", "Selected Pokemon ID : ${viewModel.selectedId.value}")
        }

        viewModel.selectedPokemon.observe(viewLifecycleOwner){pokemon ->
            pokemon?.let {
                viewModel.displayPokemon(binding, it)
            }
        }

        binding.backButtonIV.setOnClickListener {
            findNavController().navigateUp()
        }
    }
}