package com.example.rickandmorty.episode.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.rickandmorty.dao.AppDatabase
import com.example.rickandmorty.helpers.characterEntityToCharacter
import com.example.rickandmorty.helpers.convertCharacterListToEntityList
import com.example.rickandmorty.helpers.episodeEntityToEpisode
import com.example.rickandmorty.helpers.extractIdFromUri
import com.example.rickandmorty.network.ApiService
import com.example.rickandmorty.network.AppModule
import com.example.rickandmorty.network.response.Character
import com.example.rickandmorty.network.response.Episode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import retrofit2.Response

class EpisodeDetailsViewModel(private val appDatabase: AppDatabase) : ViewModel() {
    private val apiService: ApiService = AppModule.provideRetrofitInstance()
    fun getEpisodeById(episodeId: Int, isOnline: Boolean): Flow<Episode> = flow {
        val localEpisode = withContext(Dispatchers.IO) {
            appDatabase.episodeDao().getEpisodeById(episodeId)
        }
        if (localEpisode != null) emit(episodeEntityToEpisode(localEpisode))
        if (isOnline) {
            val episodeResponse: Response<Episode> = apiService.getEpisodeById(episodeId)
            if (episodeResponse.isSuccessful) {
                emit(episodeResponse.body()!!)
            }
        }
    }

    fun getCharacterForEpisode(episode: Episode, isOnline: Boolean): Flow<List<Character>> = flow {
        val characters = mutableListOf<Character>()
        try {
            if (isOnline) {
                episode.characters.forEach {
                    val characterId = extractIdFromUri(it)
                    val episodeResponse: Response<Character> =
                        apiService.getCharacterById(characterId)
                    if (episodeResponse.isSuccessful) {
                        characters.add(episodeResponse.body()!!)
                    }
                    emit(characters)
                }
                withContext(Dispatchers.IO) {
                    appDatabase.characterDao()
                        .insertAll(convertCharacterListToEntityList(characters))
                }
            } else {
                episode.characters.forEach {
                    val characterEntity = withContext(Dispatchers.IO) {
                        appDatabase.characterDao().getCharacterByUrl(it)
                    }
                    if (characterEntity != null) {
                        characters.add(characterEntityToCharacter(characterEntity))
                    }
                    emit(characters)
                }
            }
        } catch (e: Exception) {
            println(e)
        }
    }
}

class EpisodeDetailsViewModelFactory(private val appDatabase: AppDatabase) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EpisodeDetailsViewModel::class.java)) {
            return EpisodeDetailsViewModel(appDatabase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}