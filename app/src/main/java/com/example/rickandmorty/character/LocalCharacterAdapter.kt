package com.example.rickandmorty.character

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import coil.load
import com.example.rickandmorty.Constants
import com.example.rickandmorty.R
import com.example.rickandmorty.dao.entity.CharacterEntity
import com.example.rickandmorty.databinding.CharacterLayoutBinding

class LocalCharacterAdapter : ListAdapter<CharacterEntity, CharacterViewHolder>(DiffCallback) {
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
                name.text = "${currChar.name}"
                species.text = "Species: ${currChar.species}"
                status.text = "Status: ${currChar.status}"
                gender.text = "Gender: ${currChar.gender}"
                val imageLink = currChar.image
                imageView.load(imageLink) {
                    placeholder(R.drawable.loading_animation)
                    error(R.drawable.ic_broken_image)
                }
                setOnClickListener {
                    val bundle = Bundle()
                    if (currChar != null) {
                        bundle.putInt(Constants.CHARACTER, currChar.characterId)
                    }
                    findNavController().navigate(
                        R.id.action_characterFragment_to_characterDetails,
                        bundle
                    )
                }
            }
        }
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<CharacterEntity>() {
            override fun areItemsTheSame(
                oldItem: CharacterEntity,
                newItem: CharacterEntity
            ): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(
                oldItem: CharacterEntity,
                newItem: CharacterEntity
            ): Boolean {
                return oldItem.characterId == newItem.characterId
            }
        }
    }
}