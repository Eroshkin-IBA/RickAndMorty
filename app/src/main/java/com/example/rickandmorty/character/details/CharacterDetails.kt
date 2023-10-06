package com.example.rickandmorty.character.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.example.rickandmorty.Constants
import com.example.rickandmorty.databinding.FragmentCharacterDetailsBinding
import com.example.rickandmorty.helpers.extractSeasonAndEpisode
import com.example.rickandmorty.network.response.Character
import kotlinx.coroutines.launch


class CharacterDetails : Fragment() {
    private val viewModel: CharacterDetailsViewModel by viewModels()
    private lateinit var episodeAdapter: CharacterDetailsEpisodeAdapter
    private lateinit var binding: FragmentCharacterDetailsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCharacterDetailsBinding.inflate(inflater, container, false)
        val view = binding.root
        val characterId = arguments?.getInt(Constants.CHARACTER)
        episodeAdapter = CharacterDetailsEpisodeAdapter()
        if (characterId != null) {
            loadCharacter(characterId)
        }
        return view
    }

    private fun loadCharacter(characterId:Int){
        lifecycleScope.launch {
            viewModel.getCharacterById(characterId).collect { character ->
                loadData(character)
                with(binding) {
                    characterImg.load(character.image) {
                        crossfade(true)
                        crossfade(1000)
                    }
                    name.text = character.name
                    status.text = "Status: ${character.status}"
                    species.text = "Species: ${character.species}"
                    gender.text = "Gender: ${character.gender}"
                    originName.text = "Origin: ${character.origin.name}"
                    locationName.text = "Location: ${character.location.name}"
                    recyclerView.adapter = episodeAdapter
                    recyclerView.layoutManager = LinearLayoutManager(context)
                }
            }
        }
    }

    private fun loadData(character: Character) {
        lifecycleScope.launch {
            viewModel.getEpisodesForCharacter(character).collect { episodes ->
                episodeAdapter.setEpisodes(episodes)
            }
        }
    }

}