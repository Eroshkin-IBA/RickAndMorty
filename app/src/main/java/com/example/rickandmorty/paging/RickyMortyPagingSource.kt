package com.example.rickandmorty.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.rickandmorty.Constants
import com.example.rickandmorty.dao.AppDatabase
import com.example.rickandmorty.helpers.Filter
import com.example.rickandmorty.helpers.convertCharacterListToEntityList
import com.example.rickandmorty.helpers.convertToEpisodeEntities
import com.example.rickandmorty.helpers.convertToLocationEntities
import com.example.rickandmorty.network.ApiService
import com.example.rickandmorty.network.response.Character
import com.example.rickandmorty.network.response.Episode
import com.example.rickandmorty.network.response.Location
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RickyMortyPagingSource<T : Any>
    (
    private val apiService: ApiService,
    private val entityType: String,
    private val appDatabase: AppDatabase,
    private val filter: Filter = Filter()
) : PagingSource<Int, T>() {
    override fun getRefreshKey(state: PagingState<Int, T>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>):
            LoadResult<Int, T> {
        return try {
            val currentPage = params.key ?: 1
            val response = when (entityType) {
                Constants.CHARACTER -> {
                    if (filter.isAnyFieldFilled()) {
                        apiService.getCharactersPage(
                            currentPage,
                            filter.name,
                            filter.gender,
                            filter.status,
                            filter.species
                        )
                    } else {
                        apiService.getCharactersPage(currentPage)
                    }
                }

                Constants.EPISODE -> {
                    if (filter.isAnyFieldFilled()) {
                        apiService.getEpisodesPage(currentPage, filter.name)
                    } else {
                        apiService.getEpisodesPage(currentPage)
                    }
                }

                Constants.LOCATION -> {
                    if (filter.isAnyFieldFilled()) {
                        apiService.getLocationsPage(currentPage, filter.name)
                    } else {
                        apiService.getLocationsPage(currentPage)
                    }
                }

                else -> throw IllegalArgumentException("Invalid entity type: $entityType")
            }
            val responseData = mutableListOf<T>()
            val data = response.body()?.results?.map { it as T } ?: emptyList()
            responseData.addAll(data)

            withContext(Dispatchers.IO) {
                when (entityType) {
                    Constants.CHARACTER -> {
                        val characterDao = appDatabase.characterDao()
                        characterDao.insertAll(convertCharacterListToEntityList(data as List<Character>))
                    }

                    Constants.EPISODE -> {
                        val episodeDao = appDatabase.episodeDao()
                        episodeDao.insertAll(convertToEpisodeEntities(data as List<Episode>))
                    }

                    Constants.LOCATION -> {
                        val locationDao = appDatabase.locationDao()
                        locationDao.insertAll(convertToLocationEntities(data as List<Location>))
                    }
                }
            }

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