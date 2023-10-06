package com.example.rickandmorty.location.details

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.rickandmorty.Constants
import com.example.rickandmorty.R
import com.example.rickandmorty.databinding.FragmentEpisodeDetailsBinding
import com.example.rickandmorty.databinding.FragmentLocationDetailsBinding
import com.example.rickandmorty.episode.details.EpisodeDetailsCharacterAdapter
import com.example.rickandmorty.episode.details.EpisodeDetailsViewModel
import com.example.rickandmorty.network.response.Episode
import com.example.rickandmorty.network.response.Location
import kotlinx.coroutines.launch


class LocationDetailsFragment : Fragment() {
    private lateinit var binding: FragmentLocationDetailsBinding
    private val viewModel: LocationDetailsViewModel by viewModels()
    private lateinit var characterAdapter: EpisodeDetailsCharacterAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLocationDetailsBinding.inflate(inflater, container, false)
        val locationId = arguments?.getInt(Constants.LOCATION)
        characterAdapter = EpisodeDetailsCharacterAdapter(Constants.LOCATION)
        if (locationId != null) {
            loadLocation(locationId)
        }
        return binding.root
    }

    private fun loadLocation(locationId: Int) {
        lifecycleScope.launch {
            viewModel.getLocationById(locationId).collect { location ->
                loadData(location)
                with(binding) {
                    name.text = location.name
                    type.text = location.type
                    dimension.text = location.dimension
                    numberOfCharacters.text = "${location.residents.size} residents"
                    recyclerView.adapter = characterAdapter
                    recyclerView.layoutManager = GridLayoutManager(
                        context, 2
                    )
                    recyclerView.setHasFixedSize(true)
                }
            }
        }
    }

    private fun loadData(location: Location) {

        lifecycleScope.launch {
            viewModel.getCharacterForEpisode(location).collect { characters ->
                characterAdapter.setCharacters(characters)
                println(characters)
            }

        }
    }

}