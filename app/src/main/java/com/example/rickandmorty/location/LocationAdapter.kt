package com.example.rickandmorty.location

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.rickandmorty.Constants
import com.example.rickandmorty.R
import com.example.rickandmorty.databinding.LocationLayoutBinding
import com.example.rickandmorty.network.response.Location

class LocationAdapter : PagingDataAdapter<Location,
        LocationAdapter.LocationViewHolder>(diffCallback) {
    inner class LocationViewHolder(val binding: LocationLayoutBinding) :
        RecyclerView.ViewHolder(binding.root)


    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<Location>() {
            override fun areItemsTheSame(oldItem: Location, newItem: Location): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Location, newItem: Location): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationViewHolder {
        return LocationViewHolder(
            LocationLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: LocationViewHolder, position: Int) {
        val currLocation = getItem(position)
        holder.binding.apply {
            holder.itemView.apply {
                name.text = currLocation?.name
                type.text = currLocation?.type
                dimension.text = currLocation?.dimension

                setOnClickListener {
                    val bundle = Bundle()
                    if (currLocation != null) {
                        bundle.putInt(Constants.LOCATION, currLocation.id)
                    }
                    findNavController().navigate(R.id.action_locationFragment_to_locationDetailsFragment, bundle)
                }

            }
        }
    }


}