package com.example.rickandmorty.character.details

import androidx.lifecycle.ViewModel
import com.example.rickandmorty.helpers.extractIdFromUri
import com.example.rickandmorty.network.ApiService
import com.example.rickandmorty.network.AppModule
import com.example.rickandmorty.network.response.Episode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import com.example.rickandmorty.network.response.Character
import retrofit2.Response

class CharacterDetailsViewModel : ViewModel() {
    private val apiService: ApiService = AppModule.provideRetrofitInstance()

    fun getCharacterById(characterId: Int): Flow<Character> = flow {
        val episodeResponse: Response<Character> = apiService.getCharacterById(characterId)
        if (episodeResponse.isSuccessful) {
            emit(episodeResponse.body()!!)
        }
    }
    fun getEpisodesForCharacter(character: Character): Flow<List<Episode>> = flow {
        val episodes = mutableListOf<Episode>()
        character.episode.forEach {
            val episodeId = extractIdFromUri(it)
            val episodeResponse: Response<Episode> = apiService.getEpisodeById(episodeId)
            if (episodeResponse.isSuccessful) {
                episodes.add(episodeResponse.body()!!)
            }
            emit(episodes)
        }

    }
}