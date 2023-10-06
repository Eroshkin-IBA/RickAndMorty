package com.example.rickandmorty.episode.details

import android.view.View
import androidx.lifecycle.ViewModel
import com.example.rickandmorty.helpers.extractIdFromUri
import com.example.rickandmorty.network.ApiService
import com.example.rickandmorty.network.AppModule
import com.example.rickandmorty.network.response.Character
import com.example.rickandmorty.network.response.Episode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response

class EpisodeDetailsViewModel : ViewModel() {
    private val apiService: ApiService = AppModule.provideRetrofitInstance()

    fun getEpisodeById(episodeId: Int): Flow<Episode> = flow {
        val episodeResponse: Response<Episode> = apiService.getEpisodeById(episodeId)
        if (episodeResponse.isSuccessful) {
            emit(episodeResponse.body()!!)
        }
    }

    fun getCharacterForEpisode(episode: Episode): Flow<List<Character>> = flow {
        val characters = mutableListOf<Character>()
        episode.characters.forEach {
            val characterId = extractIdFromUri(it)
            val episodeResponse: Response<Character> = apiService.getCharacterById(characterId)
            if (episodeResponse.isSuccessful) {
                characters.add(episodeResponse.body()!!)
            }
            emit(characters)
        }
    }
}