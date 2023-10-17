package com.example.rickandmorty.location.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.rickandmorty.dao.AppDatabase
import com.example.rickandmorty.episode.details.EpisodeDetailsViewModel
import com.example.rickandmorty.helpers.characterEntityToCharacter
import com.example.rickandmorty.helpers.convertCharacterListToEntityList
import com.example.rickandmorty.helpers.extractIdFromUri
import com.example.rickandmorty.helpers.locationEntityToLocation
import com.example.rickandmorty.network.ApiService
import com.example.rickandmorty.network.AppModule
import com.example.rickandmorty.network.response.Character
import com.example.rickandmorty.network.response.Episode
import com.example.rickandmorty.network.response.Location
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import retrofit2.Response

class LocationDetailsViewModel(private val appDatabase: AppDatabase) : ViewModel() {
    private val apiService: ApiService = AppModule.provideRetrofitInstance()
    fun getLocationById(locationId: Int, isOnline: Boolean): Flow<Location> = flow {
        val localLocation = withContext(Dispatchers.IO) {
            appDatabase.locationDao().getLocationById(locationId)
        }
        if (localLocation != null) emit(locationEntityToLocation(localLocation))
        if (isOnline) {
            val episodeResponse: Response<Location> = apiService.getLocationById(locationId)
            if (episodeResponse.isSuccessful) {
                emit(episodeResponse.body()!!)
            }
        }
    }

    fun getCharacterForEpisode(location: Location, isOnline: Boolean): Flow<List<Character>> =
        flow {
            val characters = mutableListOf<Character>()
            if (isOnline) {
                location.residents.forEach {
                    if (it.length > 1) {
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
                }
            } else {
                location.residents.forEach {
                    if (it.length > 1) {
                        val characterEntity = withContext(Dispatchers.IO) {
                            appDatabase.characterDao().getCharacterByUrl(it)
                        }
                        if (characterEntity != null) {
                            characters.add(characterEntityToCharacter(characterEntity))
                        }
                        emit(characters)
                    }
                }
            }
        }
}

class LocationDetailsViewModelFactory(private val appDatabase: AppDatabase) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LocationDetailsViewModel::class.java)) {
            return LocationDetailsViewModel(appDatabase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}