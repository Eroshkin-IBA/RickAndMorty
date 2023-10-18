package com.example.rickandmorty.episode

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.rickandmorty.Constants
import com.example.rickandmorty.R
import com.example.rickandmorty.dao.entity.EpisodeEntity
import com.example.rickandmorty.databinding.EpisodeLayoutBinding
import com.example.rickandmorty.helpers.extractSeasonAndEpisode

class LocalEpisodeAdapter : ListAdapter<EpisodeEntity, EpisodeViewHolder>(DiffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EpisodeViewHolder {
        return EpisodeViewHolder(
            EpisodeLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: EpisodeViewHolder, position: Int) {
        val currEpisode = getItem(position)
        val (formattedSeason, formattedSeries) = currEpisode?.let { extractSeasonAndEpisode(it.episode) }!!
        holder.binding.apply {
            holder.itemView.apply {
                name.text = currEpisode?.name
                season.text = formattedSeason
                series.text = formattedSeries
                numOfCharacters.text = "${currEpisode.characters.split(", ").size} characters"
                date.text = currEpisode?.air_date
                setOnClickListener {
                    val bundle = Bundle()
                    bundle.putInt(Constants.EPISODE, currEpisode.id)
                    findNavController().navigate(
                        R.id.action_episodeFragment_to_episodeDetails,
                        bundle
                    )
                }
            }
        }
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<EpisodeEntity>() {
            override fun areItemsTheSame(
                oldItem: EpisodeEntity,
                newItem: EpisodeEntity
            ): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(
                oldItem: EpisodeEntity,
                newItem: EpisodeEntity
            ): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}