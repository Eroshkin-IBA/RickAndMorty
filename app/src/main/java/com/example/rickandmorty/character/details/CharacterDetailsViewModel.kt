package com.example.rickandmorty.character.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.rickandmorty.character.CharacterViewModel
import com.example.rickandmorty.dao.AppDatabase
import com.example.rickandmorty.helpers.characterEntityToCharacter
import com.example.rickandmorty.helpers.convertToEpisodeEntities
import com.example.rickandmorty.helpers.episodeEntityToEpisode
import com.example.rickandmorty.helpers.extractIdFromUri
import com.example.rickandmorty.network.ApiService
import com.example.rickandmorty.network.AppModule
import com.example.rickandmorty.network.response.Episode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import com.example.rickandmorty.network.response.Character
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

class CharacterDetailsViewModel(private val appDatabase: AppDatabase) : ViewModel() {
    private val apiService: ApiService = AppModule.provideRetrofitInstance()

    fun getCharacterById(characterId: Int, isOnline: Boolean): Flow<Character> = flow {
        val localCharacter = withContext(Dispatchers.IO) {
            appDatabase.characterDao().getCharacterById(characterId)
        }

        if (localCharacter != null) emit(characterEntityToCharacter(localCharacter))

        if (isOnline) {
            val characterResponse: Response<Character> = apiService.getCharacterById(characterId)
            if (characterResponse.isSuccessful) {
                emit(characterResponse.body()!!)
            }
        }
    }

    fun getEpisodesForCharacter(character: Character, isOnline: Boolean): Flow<List<Episode>> =
        flow {
            val episodes = mutableListOf<Episode>()

            if (isOnline) {
                character.episode.forEach {
                    val episodeId = extractIdFromUri(it)
                    val episodeResponse: Response<Episode> = apiService.getEpisodeById(episodeId)
                    if (episodeResponse.isSuccessful) {
                        episodes.add(episodeResponse.body()!!)
                    }
                    emit(episodes)
                }
                withContext(Dispatchers.IO) {
                    appDatabase.episodeDao().insertAll(convertToEpisodeEntities(episodes))
                }
            } else {
                character.episode.forEach {
                    val episode = withContext(Dispatchers.IO) {
                        appDatabase.episodeDao().getEpisodeByUrl(it)
                    }
                    if (episode != null) {
                        episodes.add(episodeEntityToEpisode(episode))
                        emit(episodes)
                    }
                }
            }
        }

}

class CharacterDetailsViewModelFactory(private val appDatabase: AppDatabase) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CharacterDetailsViewModel::class.java)) {
            return CharacterDetailsViewModel(appDatabase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}