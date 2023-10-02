package com.example.rickandmorty.network

import com.example.rickandmorty.network.response.Character
import com.example.rickandmorty.network.response.CharactersPage
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


private const val CHARACTER = "character"

interface ApiService {
    @GET("${CHARACTER}/{character-id}")
    suspend fun getCharacterById(
        @Path("character-id") characterId: Int
    ): Response<Character>
    @GET("${CHARACTER}/")
    suspend fun getCharactersPage(
        @Query("page") pageIndex: Int
    ): Response<CharactersPage>
}

