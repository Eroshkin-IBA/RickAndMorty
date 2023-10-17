package com.example.rickandmorty.location

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.rickandmorty.R
import com.example.rickandmorty.dao.AppDatabase
import com.example.rickandmorty.databinding.FragmentLocationBinding
import com.example.rickandmorty.helpers.Filter
import com.example.rickandmorty.network.isOnline
import kotlinx.coroutines.launch


class LocationFragment : Fragment() {
    private lateinit var binding: FragmentLocationBinding
    private val locationAdapter by lazy {
        LocationAdapter()
    }
    private val localLocationAdapter by lazy {
        LocalLocationAdapter()
    }
    private val viewModel: LocationViewModel by viewModels() {
        LocationViewModelFactory(AppDatabase.getDataBase(requireContext()))
    }
    private lateinit var filter: Filter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        filter = Filter()
        binding = FragmentLocationBinding.inflate(inflater, container, false)
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
        return binding.root
    }

    private fun setupRecyclerView() {
        binding.recyclerView.adapter = locationAdapter
        binding.recyclerView.apply {
            layoutManager = GridLayoutManager(
                context, 2
            )
            setHasFixedSize(true)
        }
    }

    private fun loadData() {
        if (isOnline(requireContext())) {
            binding.recyclerView.adapter = locationAdapter
            lifecycleScope.launch {
                viewModel.listData.collect {
                    locationAdapter.submitData(it)
                }
            }
        } else {
            binding.recyclerView.adapter = localLocationAdapter
            lifecycleScope.launch {
                viewModel.localData.collect {
                    localLocationAdapter.submitList(it)
                }
            }
        }
    }

    private fun filterList(filter: Filter) {
        if (filter.isAnyFieldFilled()) {
            if (isOnline(requireContext())) {
                binding.recyclerView.adapter = locationAdapter
                lifecycleScope.launch {
                    viewModel.searchLocation(filter).collect {
                        locationAdapter.submitData(it)
                    }
                }
            } else {
                binding.recyclerView.adapter = localLocationAdapter
                lifecycleScope.launch {
                    viewModel.searchLocationInCache(filter.name).collect { characterList ->
                        if (characterList.isEmpty()) Toast.makeText(
                            requireContext(),
                            R.string.searchAnswer,
                            Toast.LENGTH_SHORT
                        ).show()
                        localLocationAdapter.submitList(characterList)
                    }
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
}



