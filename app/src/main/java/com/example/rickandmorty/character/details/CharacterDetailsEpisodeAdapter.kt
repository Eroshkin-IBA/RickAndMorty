package com.example.rickandmorty.character.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.rickandmorty.Constants
import com.example.rickandmorty.R
import com.example.rickandmorty.databinding.EpisodeLayoutBinding
import com.example.rickandmorty.helpers.extractSeasonAndEpisode
import com.example.rickandmorty.network.response.Episode

class CharacterDetailsEpisodeAdapter :
    RecyclerView.Adapter<CharacterDetailsEpisodeAdapter.EpisodeViewHolder>() {

    private val episodes: MutableList<Episode> = mutableListOf()
    fun setEpisodes(episodes: List<Episode>) {
        this.episodes.clear()
        this.episodes.addAll(episodes)
        notifyDataSetChanged()
    }

    inner class EpisodeViewHolder(val binding: EpisodeLayoutBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EpisodeViewHolder {
        return EpisodeViewHolder(
            EpisodeLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )
    }

    override fun getItemCount(): Int {
        return episodes.size
    }

    override fun onBindViewHolder(holder: EpisodeViewHolder, position: Int) {
        val currEpisode = episodes[position]
        val (formattedSeason, formattedSeries) = extractSeasonAndEpisode(currEpisode.episode)!!

        holder.binding.apply {
            holder.itemView.apply {
                name.text = currEpisode.name
                season.text = formattedSeason
                series.text = formattedSeries
                numOfCharacters.text = "${currEpisode.characters.size} characters"
                date.text = currEpisode.air_date

                setOnClickListener {
                    val bundle = Bundle()
                    bundle.putInt(Constants.EPISODE, currEpisode.id)
                    findNavController().navigate(
                        R.id.action_characterDetails_to_episodeDetails,
                        bundle
                    )
                }
            }
        }
    }
}

