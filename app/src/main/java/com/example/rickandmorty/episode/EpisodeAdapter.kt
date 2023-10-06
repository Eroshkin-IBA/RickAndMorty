package com.example.rickandmorty.episode

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.rickandmorty.Constants
import com.example.rickandmorty.R
import com.example.rickandmorty.databinding.EpisodeLayoutBinding
import com.example.rickandmorty.helpers.extractSeasonAndEpisode
import com.example.rickandmorty.network.response.Episode

class EpisodeAdapter : PagingDataAdapter<Episode,
        EpisodeAdapter.EpisodeViewHolder>(diffCallback) {
    inner class EpisodeViewHolder(val binding: EpisodeLayoutBinding) :
        RecyclerView.ViewHolder(binding.root)

    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<Episode>() {
            override fun areItemsTheSame(oldItem: Episode, newItem: Episode): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Episode, newItem: Episode): Boolean {
                return oldItem == newItem
            }
        }
    }


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
                numOfCharacters.text = "${currEpisode?.characters?.size} characters"
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

}

