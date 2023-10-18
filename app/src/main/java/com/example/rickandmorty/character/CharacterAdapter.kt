package com.example.rickandmorty.character

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import coil.load
import com.example.rickandmorty.Constants
import com.example.rickandmorty.R
import com.example.rickandmorty.databinding.CharacterLayoutBinding
import com.example.rickandmorty.network.response.Character

class CharacterAdapter : PagingDataAdapter<Character,
        CharacterViewHolder>(diffCallback) {
    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<Character>() {
            override fun areItemsTheSame(oldItem: Character, newItem: Character): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Character, newItem: Character): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        return CharacterViewHolder(
            CharacterLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        val currChar = getItem(position)
        holder.binding.apply {
            holder.itemView.apply {
                name.text = "${currChar?.name}"
                species.text = "Species: ${currChar?.species}"
                status.text = "Status: ${currChar?.status}"
                gender.text = "Gender: ${currChar?.gender}"
                val imageLink = currChar?.image
                imageView.load(imageLink) {
                    placeholder(R.drawable.loading_animation)
                    error(R.drawable.ic_broken_image)
                }
                setOnClickListener {
                    val bundle = Bundle()
                    if (currChar != null) {
                        bundle.putInt(Constants.CHARACTER, currChar.id)
                    }
                    findNavController().navigate(
                        R.id.action_characterFragment_to_characterDetails,
                        bundle
                    )
                }
            }
        }
    }
}

