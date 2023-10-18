package com.example.rickandmorty.episode.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.rickandmorty.Constants
import com.example.rickandmorty.R
import com.example.rickandmorty.character.CharacterViewHolder
import com.example.rickandmorty.databinding.CharacterLayoutBinding
import com.example.rickandmorty.network.response.Character

class EpisodeDetailsCharacterAdapter(checker: String) :
    RecyclerView.Adapter<CharacterViewHolder>() {
    private val checker = checker
    private val characters: MutableList<Character> = mutableListOf()
    fun setCharacters(character: List<Character>) {
        this.characters.clear()
        this.characters.addAll(character)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        return CharacterViewHolder(
            CharacterLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )
    }

    override fun getItemCount(): Int {
        return characters.size
    }

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        val currChar = characters[position]
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
                    when (checker) {
                        Constants.EPISODE -> findNavController().navigate(
                            R.id.action_episodeDetails_to_characterDetails,
                            bundle
                        )

                        Constants.LOCATION -> findNavController().navigate(
                            R.id.action_locationDetailsFragment_to_characterDetails,
                            bundle
                        )
                    }
                }
            }
        }
    }
}