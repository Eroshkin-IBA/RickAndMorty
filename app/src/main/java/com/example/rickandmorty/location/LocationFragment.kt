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
    private lateinit var searchFilter: Filter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        searchFilter = Filter()
        binding = FragmentLocationBinding.inflate(inflater, container, false)
        setupRecyclerView()
        loadData()
        with(binding) {
            showFilterBtn.setOnClickListener {
                if (filter.visibility == View.GONE) {
                    filter.visibility = View.VISIBLE
                } else {
                    filter.visibility = View.GONE
                }
            }
            btnApplyFilter.setOnClickListener {
                searchFilter.name = searchView.query.toString()
                searchFilter.type = typePlainText.text.toString()
                searchFilter.dimension = dimensionPlainText.text.toString()
                filterList(searchFilter)
                filter.visibility = View.GONE
                resetFilterBtn.visibility = View.VISIBLE
            }
            resetFilterBtn.setOnClickListener {
                typePlainText.setText("")
                dimensionPlainText.setText("")
                searchFilter.resetFilter()
                filterList(searchFilter)
                resetFilterBtn.visibility = View.GONE
            }
            swipeRefreshLayout.setOnRefreshListener {
                noInternet.visibility = View.GONE
                searchView.setQuery("", false)
                typePlainText.setText("")
                dimensionPlainText.setText("")
                resetFilterBtn.visibility = View.GONE
                searchFilter.resetFilter()
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
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
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
                viewModel.searchLocationInCache(filter).collect { characterList ->
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

    private fun noInternetMessage() {
        if (!isOnline(requireContext())) {
            binding.noInternet.visibility = View.VISIBLE
        }
    }
}



