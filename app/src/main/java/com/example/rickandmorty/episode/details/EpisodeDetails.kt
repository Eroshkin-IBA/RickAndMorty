package com.example.rickandmorty.episode.details

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
import com.example.rickandmorty.character.details.CharacterDetailsEpisodeAdapter
import com.example.rickandmorty.character.details.CharacterDetailsViewModel
import com.example.rickandmorty.databinding.FragmentCharacterDetailsBinding
import com.example.rickandmorty.databinding.FragmentEpisodeDetailsBinding
import com.example.rickandmorty.helpers.extractSeasonAndEpisode
import com.example.rickandmorty.network.response.Character
import com.example.rickandmorty.network.response.Episode
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch


class EpisodeDetails : Fragment() {
    private lateinit var binding: FragmentEpisodeDetailsBinding
    private val viewModel: EpisodeDetailsViewModel by viewModels()
    private lateinit var characterAdapter: EpisodeDetailsCharacterAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEpisodeDetailsBinding.inflate(inflater, container, false)
        val episodeId = arguments?.getInt(Constants.EPISODE)
        characterAdapter = EpisodeDetailsCharacterAdapter(Constants.EPISODE)

        if (episodeId != null) {
            loadEpisode(episodeId)
        }
        return binding.root
    }

    private fun loadEpisode(episodeId: Int) {
        lifecycleScope.launch {
            viewModel.getEpisodeById(episodeId).collect { episode ->
                val (formattedSeason, formattedSeries) = episode?.let {
                    extractSeasonAndEpisode(
                        it.episode
                    )
                }!!
                loadCharacters(episode)
                with(binding) {
                    name.text = episode.name
                    season.text = formattedSeason
                    series.text = formattedSeries
                    date.text = episode.air_date
                    numberOfCharacters.text = "${episode.characters.size} characters"
                    recyclerView.adapter = characterAdapter
                    recyclerView.layoutManager = GridLayoutManager(
                        context, 2
                    )
                    recyclerView.setHasFixedSize(true)
                }
            }
        }
    }


    private fun loadCharacters(episode: Episode) {
        lifecycleScope.launch {
            viewModel.getCharacterForEpisode(episode).collect { characters ->
                characterAdapter.setCharacters(characters)
                println(characters)
            }

        }
    }

}