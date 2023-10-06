package com.example.rickandmorty.episode

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.rickandmorty.character.CharacterAdapter
import com.example.rickandmorty.character.CharacterViewModel
import com.example.rickandmorty.databinding.FragmentCharacterBinding
import com.example.rickandmorty.databinding.FragmentEpisodeBinding
import kotlinx.coroutines.launch

class EpisodeFragment : Fragment() {
    private lateinit var binding: FragmentEpisodeBinding
    private lateinit var episodeAdapter: EpisodeAdapter
    private val viewModel: EpisodeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEpisodeBinding.inflate(inflater, container, false)
        val view = binding.root
        setupRecyclerView()
        loadData()
        return view
    }

    private fun setupRecyclerView() {

        episodeAdapter = EpisodeAdapter()

        binding.recyclerView.apply {
            adapter = episodeAdapter
            layoutManager = GridLayoutManager(
                context, 2
            )
            setHasFixedSize(true)
        }

    }

    private fun loadData() {

        lifecycleScope.launch {
            viewModel.listData.collect {

                Log.d("EpisodeFragment", "load: $it")
                episodeAdapter.submitData(it)
            }

        }
    }
}



