package com.example.rickandmorty.location.details

import androidx.lifecycle.ViewModel
import com.example.rickandmorty.helpers.extractIdFromUri
import com.example.rickandmorty.network.ApiService
import com.example.rickandmorty.network.AppModule
import com.example.rickandmorty.network.response.Character
import com.example.rickandmorty.network.response.Episode
import com.example.rickandmorty.network.response.Location
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response

class LocationDetailsViewModel : ViewModel() {
    private val apiService: ApiService = AppModule.provideRetrofitInstance()
    fun getLocationById(locationId: Int): Flow<Location> = flow {
        val episodeResponse: Response<Location> = apiService.getLocationById(locationId)
        if (episodeResponse.isSuccessful) {
            emit(episodeResponse.body()!!)
        }
    }
    fun getCharacterForEpisode(location: Location): Flow<List<Character>> = flow {
        val characters = mutableListOf<Character>()
        location.residents.forEach {
            val characterId = extractIdFromUri(it)
            val episodeResponse: Response<Character> = apiService.getCharacterById(characterId)
            if (episodeResponse.isSuccessful) {
                characters.add(episodeResponse.body()!!)
            }
            emit(characters)
        }

    }
}