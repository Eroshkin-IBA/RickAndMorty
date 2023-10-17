package com.example.rickandmorty.location

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.rickandmorty.Constants
import com.example.rickandmorty.R
import com.example.rickandmorty.dao.entity.LocationEntity
import com.example.rickandmorty.databinding.LocationLayoutBinding

class LocalLocationAdapter :
    ListAdapter<LocationEntity, LocationViewHolder>(LocalLocationAdapter.DiffCallback) {

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
                    findNavController().navigate(
                        R.id.action_locationFragment_to_locationDetailsFragment,
                        bundle
                    )
                }

            }
        }
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<LocationEntity>() {
            override fun areItemsTheSame(
                oldItem: LocationEntity,
                newItem: LocationEntity
            ): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(
                oldItem: LocationEntity,
                newItem: LocationEntity
            ): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}