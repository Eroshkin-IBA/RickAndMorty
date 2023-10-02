package com.example.rickandmorty.character

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.rickandmorty.network.ApiService
import com.example.rickandmorty.network.response.Character

class RickyMortyPagingSource
    (
    private val apiService: ApiService
) : PagingSource<Int, Character>() {

    override fun getRefreshKey(state: PagingState<Int, Character>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>):
            LoadResult<Int, Character> {

        return try {
            val currentPage = params.key ?: 1
            val response = apiService.getCharactersPage(currentPage)
            val responseData = mutableListOf<Character>()
            val data = response.body()?.results ?: emptyList()
            responseData.addAll(data)

            LoadResult.Page(
                data = responseData,
                prevKey = if (currentPage == 1) null else -1,
                nextKey = currentPage.plus(1)
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }

    }
}