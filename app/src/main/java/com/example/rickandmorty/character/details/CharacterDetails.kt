package com.example.rickandmorty.character.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import coil.ImageLoader
import coil.load
import com.example.rickandmorty.BaseFragment
import com.example.rickandmorty.Constants
import com.example.rickandmorty.R
import com.example.rickandmorty.dao.AppDatabase
import com.example.rickandmorty.databinding.FragmentCharacterDetailsBinding
import com.example.rickandmorty.helpers.extractIdFromUri
import com.example.rickandmorty.helpers.loadWithFallback
import com.example.rickandmorty.network.isOnline
import com.example.rickandmorty.network.response.Character
import kotlinx.coroutines.launch


class CharacterDetails : BaseFragment() {
    private val viewModel: CharacterDetailsViewModel by viewModels {
        CharacterDetailsViewModelFactory(AppDatabase.getDataBase(requireContext()))
    }
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
        loadCharacter(characterId!!)
        binding.swipeRefreshLayout.setOnRefreshListener {
            loadCharacter(characterId!!)
            noInternetMessage()
            binding.swipeRefreshLayout.isRefreshing = false
        }
        noInternetMessage()
        return view
    }

    private fun loadCharacter(characterId: Int) {
        lifecycleScope.launch {
            viewModel.getCharacterById(characterId, isOnline(requireContext()))
                .collect { character ->
                    loadData(character, isOnline(requireContext()))
                    with(binding) {
                        characterImg.load(character.image) {
                            placeholder(R.drawable.loading_animation)
                            error(R.drawable.ic_broken_image)
                        }
                        name.text = character.name
                        status.text = "Status: ${character.status}"
                        species.text = "Species: ${character.species}"
                        gender.text = "Gender: ${character.gender}"
                        locationName.text = "Location: ${character.location.name}"
                        locationName.setOnClickListener {
                            val bundle = Bundle()
                            bundle.putInt(
                                Constants.LOCATION,
                                extractIdFromUri(character.location.url)
                            )
                            findNavController().navigate(
                                R.id.action_characterDetails_to_locationDetailsFragment,
                                bundle
                            )
                        }
                        recyclerView.adapter = episodeAdapter
                        recyclerView.layoutManager = LinearLayoutManager(context)
                    }
                }
        }
    }

    private fun loadData(character: Character, isOnline: Boolean) {
        lifecycleScope.launch {
            viewModel.getEpisodesForCharacter(character, isOnline).collect { episodes ->
                episodeAdapter.setEpisodes(episodes)
            }
        }
    }

}