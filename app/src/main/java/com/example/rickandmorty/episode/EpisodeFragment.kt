package com.example.rickandmorty.episode

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.rickandmorty.R
import com.example.rickandmorty.dao.AppDatabase
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
    private lateinit var searchFilter: Filter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        searchFilter = Filter()
        binding = FragmentEpisodeBinding.inflate(inflater, container, false)
        setupRecyclerView()
        loadData()
        val seasonAdapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.seasons,
            R.layout.spinner_layout
        )
        seasonAdapter.setDropDownViewResource(R.layout.spinner_dropdown)
        with(binding) {
            spinnerSeason.adapter = seasonAdapter
            showFilterBtn.setOnClickListener {
                if (filter.visibility == View.GONE) {
                    filter.visibility = View.VISIBLE
                } else {
                    filter.visibility = View.GONE
                }
            }
            btnApplyFilter.setOnClickListener {
                searchFilter.name = searchView.query.toString()
                searchFilter.season = spinnerSeason.selectedItem.toString()
                filterList(searchFilter)
                filter.visibility = View.GONE
                resetFilterBtn.visibility = View.VISIBLE
            }
            resetFilterBtn.setOnClickListener {
                spinnerSeason.setSelection(0)
                searchFilter.resetFilter()
                filterList(searchFilter)
                resetFilterBtn.visibility = View.GONE
            }
            swipeRefreshLayout.setOnRefreshListener {
                searchView.setQuery("", false)
                binding.noInternet.visibility = View.GONE
                spinnerSeason.setSelection(0)
                searchFilter.resetFilter()
                resetFilterBtn.visibility = View.GONE
                setupRecyclerView()
                loadData()
                noInternetMessage()
                swipeRefreshLayout.isRefreshing = false
            }
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    if (newText != null) {
                        searchFilter.name = newText
                        filterList(searchFilter)
                    }
                    return true
                }
            })
        }
        noInternetMessage()
        return binding.root
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
            binding.noInternet.visibility = View.VISIBLE
        }
    }

    private fun filterList(filter: Filter) {
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
                viewModel.searchEpisodeInCache(filter).collect { episodeList ->
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



