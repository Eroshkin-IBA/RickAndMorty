package com.example.rickandmorty.character

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
import com.example.rickandmorty.databinding.FragmentCharacterBinding
import com.example.rickandmorty.helpers.Filter
import com.example.rickandmorty.network.isOnline
import kotlinx.coroutines.launch

class CharacterFragment : Fragment(R.layout.fragment_character) {
    private lateinit var binding: FragmentCharacterBinding
    private val characterAdapter by lazy { CharacterAdapter() }
    private val localCharacterAdapter by lazy { LocalCharacterAdapter() }
    private val viewModel: CharacterViewModel by viewModels {
        CharacterViewModelFactory(AppDatabase.getDataBase(requireContext()))
    }
    private lateinit var searchFilter: Filter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        searchFilter = Filter()
        binding = FragmentCharacterBinding.inflate(inflater, container, false)
        val genderAdapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.gender,
            R.layout.spinner_layout
        )
        genderAdapter.setDropDownViewResource(R.layout.spinner_dropdown)
        val speciesAdapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.species,
            R.layout.spinner_layout
        )
        speciesAdapter.setDropDownViewResource(R.layout.spinner_dropdown)
        val statusAdapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.status,
            R.layout.spinner_layout
        )
        statusAdapter.setDropDownViewResource(R.layout.spinner_dropdown)
        with(binding) {
            spinnerSpecies.adapter = speciesAdapter
            spinnerGender.adapter = genderAdapter
            spinnerStatus.adapter = statusAdapter
            showFilterBtn.setOnClickListener {
                if (filter.visibility == View.GONE) {
                    filter.visibility = View.VISIBLE
                } else {
                    filter.visibility = View.GONE
                }
            }
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    searchFilter.name = newText!!
                    filterList(searchFilter)
                    return true
                }
            })
            swipeRefreshLayout.setOnRefreshListener {
                noInternet.visibility = View.GONE
                spinnerGender.setSelection(0)
                spinnerStatus.setSelection(0)
                spinnerSpecies.setSelection(0)
                searchFilter.resetFilter()
                resetFilterBtn.visibility = View.GONE
                searchView.setQuery("", false)
                loadData()
                noInternetMessage()
                swipeRefreshLayout.isRefreshing = false
            }
            btnApplyFilter.setOnClickListener {
                searchFilter.name = searchView.query.toString()
                searchFilter.gender = spinnerGender.selectedItem.toString()
                searchFilter.status = spinnerStatus.selectedItem.toString()
                searchFilter.species = spinnerSpecies.selectedItem.toString()
                filterList(searchFilter)
                filter.visibility = View.GONE
                resetFilterBtn.visibility = View.VISIBLE
            }
            resetFilterBtn.setOnClickListener {
                spinnerGender.setSelection(0)
                spinnerStatus.setSelection(0)
                spinnerSpecies.setSelection(0)
                searchFilter.resetFilter()
                filterList(searchFilter)
                resetFilterBtn.visibility = View.GONE
            }
        }
        setupRecyclerView()
        loadData()
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
            binding.recyclerView.adapter = characterAdapter
            lifecycleScope.launch {
                viewModel.listData.collect {
                    characterAdapter.submitData(it)
                }
            }
        } else {
            binding.recyclerView.adapter = localCharacterAdapter
            lifecycleScope.launch {
                viewModel.localListData.collect {
                    localCharacterAdapter.submitList(it)
                }
            }
        }
    }

    private fun filterList(filter: Filter) {
        if (isOnline(requireContext())) {
            binding.recyclerView.adapter = characterAdapter
            lifecycleScope.launch {
                viewModel.searchCharacter(filter).collect {
                    characterAdapter.submitData(it)
                }
            }
        } else {
            binding.recyclerView.adapter = localCharacterAdapter
            lifecycleScope.launch {
                viewModel.searchCharacterInCache(filter).collect { characterList ->
                    if (characterList.isEmpty()) Toast.makeText(
                        requireContext(),
                        R.string.searchAnswer,
                        Toast.LENGTH_SHORT
                    ).show()
                    localCharacterAdapter.submitList(characterList)
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