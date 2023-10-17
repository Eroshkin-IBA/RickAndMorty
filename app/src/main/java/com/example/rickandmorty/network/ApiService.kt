package com.example.rickandmorty.network

import com.example.rickandmorty.Constants
import com.example.rickandmorty.network.response.Character
import com.example.rickandmorty.network.response.Episode
import com.example.rickandmorty.network.response.Location
import com.example.rickandmorty.network.response.Page
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface ApiService {
    @GET("${Constants.CHARACTER}/{character-id}")
    suspend fun getCharacterById(
        @Path("character-id") characterId: Int
    ): Response<Character>

    @GET("${Constants.CHARACTER}/")
    suspend fun getCharactersPage(
        @Query("page") pageIndex: Int
    ): Response<Page<Character>>

    @GET("${Constants.CHARACTER}/")
    suspend fun getCharactersPage(
        @Query("page") pageIndex: Int,
        @Query("name") characterName: String,
        @Query("gender") characterGender: String,
        @Query("status") characterStatus: String,
        @Query("species") characterSpecies: String
    ): Response<Page<Character>>

    @GET("${Constants.EPISODE}/")
    suspend fun getEpisodesPage(
        @Query("page") pageIndex: Int,
        @Query("name") episodeName: String
    ): Response<Page<Episode>>

    @GET("${Constants.EPISODE}/")
    suspend fun getEpisodesPage(
        @Query("page") pageIndex: Int
    ): Response<Page<Episode>>

    @GET("${Constants.LOCATION}/")
    suspend fun getLocationsPage(
        @Query("page") pageIndex: Int
    ): Response<Page<Location>>

    @GET("${Constants.LOCATION}/")
    suspend fun getLocationsPage(
        @Query("page") pageIndex: Int,
        @Query("name") locationName: String
    ): Response<Page<Location>>

    @GET("${Constants.LOCATION}/{location-id}")
    suspend fun getLocationById(
        @Path("location-id") locationId: Int
    ): Response<Location>

    @GET("${Constants.EPISODE}/{episode-id}")
    suspend fun getEpisodeById(
        @Path("episode-id") episodeId: Int
    ): Response<Episode>
}

