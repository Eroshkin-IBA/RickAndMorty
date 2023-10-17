package com.example.rickandmorty.episode

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.rickandmorty.MyApplication
import com.example.rickandmorty.R
import com.example.rickandmorty.character.CharacterAdapter
import com.example.rickandmorty.character.CharacterViewModel
import com.example.rickandmorty.character.CharacterViewModelFactory
import com.example.rickandmorty.dao.AppDatabase
import com.example.rickandmorty.databinding.FragmentCharacterBinding
import com.example.rickandmorty.databinding.FragmentEpisodeBinding
import com.example.rickandmorty.helpers.Filter
import com.example.rickandmorty.network.isOnline
import kotlinx.coroutines.launch

class EpisodeFragment : Fragment() {
    private lateinit var binding: FragmentEpisodeBinding

    private val episodeAdapter by lazy { EpisodeAdapter() }
    private val localEpisodeAdapter by lazy {
        LocalEpisodeAdapter()
    }
    private val viewModel: EpisodeViewModel by viewModels() {
        EpisodeViewModelFactory(AppDatabase.getDataBase(requireContext()))
    }
    private lateinit var filter: Filter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        filter = Filter()
        binding = FragmentEpisodeBinding.inflate(inflater, container, false)
        val view = binding.root
        setupRecyclerView()
        loadData()
        binding.swipeRefreshLayout.setOnRefreshListener {
            setupRecyclerView()
            loadData()
            noInternetMessage()
            binding.swipeRefreshLayout.isRefreshing = false
        }
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    filter.name = newText
                    filterList(filter)
                }
                return true
            }
        })
        noInternetMessage()
        return view
    }

    private fun setupRecyclerView() {
        binding.recyclerView.apply {
            layoutManager = GridLayoutManager(
                context, 2
            )
            setHasFixedSize(true)
        }

    }

    private fun loadData() {
        if (isOnline(requireContext())) {
            binding.recyclerView.adapter = episodeAdapter
            lifecycleScope.launch {
                viewModel.listData.collect {
                    episodeAdapter.submitData(it)
                }
            }
        } else {
            binding.recyclerView.adapter = localEpisodeAdapter
            lifecycleScope.launch {
                viewModel.localData.collect {
                    localEpisodeAdapter.submitList(it)
                }
            }
        }
    }

    private fun noInternetMessage() {
        if (!isOnline(requireContext())) {
            Toast.makeText(
                requireContext(),
                "there is no connection",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun filterList(filter: Filter) {
        if (filter.isAnyFieldFilled()) {
            if (isOnline(requireContext())) {
                binding.recyclerView.adapter = episodeAdapter
                lifecycleScope.launch {
                    viewModel.searchEpisode(filter).collect {
                        episodeAdapter.submitData(it)
                    }
                }
            } else {
                binding.recyclerView.adapter = localEpisodeAdapter
                lifecycleScope.launch {
                    viewModel.searchEpisodeInCache(filter.name).collect { episodeList ->
                        if (episodeList.isEmpty()) Toast.makeText(
                            requireContext(),
                            R.string.searchAnswer,
                            Toast.LENGTH_SHORT
                        ).show()
                        localEpisodeAdapter.submitList(episodeList)
                    }
                }
            }
        }
    }

}



