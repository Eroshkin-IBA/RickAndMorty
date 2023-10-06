package com.example.rickandmorty.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.rickandmorty.Constants
import com.example.rickandmorty.network.ApiService

class RickyMortyPagingSource<T : Any>
    (
    private val apiService: ApiService,
    private val entityType: String
) : PagingSource<Int, T>() {

    override fun getRefreshKey(state: PagingState<Int, T>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>):
            LoadResult<Int, T> {

        return try {
            val currentPage = params.key ?: 1
            val response = when(entityType){
                Constants.CHARACTER -> apiService.getCharactersPage(currentPage)
                Constants.EPISODE -> apiService.getEpisodesPage(currentPage)
Constants.LOCATION -> apiService.getLocationsPage(currentPage)
                else -> throw IllegalArgumentException("Invalid entity type: $entityType")
            }
            val responseData = mutableListOf<T>()
            val data = response.body()?.results?.map { it as T }?: emptyList()
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