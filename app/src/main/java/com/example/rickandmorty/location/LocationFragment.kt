package com.example.rickandmorty.location

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.rickandmorty.databinding.FragmentLocationBinding
import kotlinx.coroutines.launch


class LocationFragment : Fragment() {
    private lateinit var binding: FragmentLocationBinding
    private lateinit var locationAdapter: LocationAdapter
    private val viewModel: LocationViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLocationBinding.inflate(inflater, container, false)
        setupRecyclerView()
        loadData()
        // Inflate the layout for this fragment
        return binding.root
    }

    private fun setupRecyclerView() {

        locationAdapter = LocationAdapter()

        binding.recyclerView.apply {
            adapter = locationAdapter
            layoutManager = GridLayoutManager(
                context, 2
            )
            setHasFixedSize(true)
        }

    }

    private fun loadData() {

        lifecycleScope.launch {
            viewModel.listData.collect {

                Log.d("LocationFragment", "load: $it")
                locationAdapter.submitData(it)
            }

        }
    }


}



