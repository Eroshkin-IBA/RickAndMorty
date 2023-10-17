package com.example.rickandmorty.episode

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.rickandmorty.Constants
import com.example.rickandmorty.dao.AppDatabase
import com.example.rickandmorty.dao.entity.EpisodeEntity
import com.example.rickandmorty.helpers.Filter
import com.example.rickandmorty.network.AppModule
import com.example.rickandmorty.network.response.Episode
import com.example.rickandmorty.paging.RickyMortyPagingSource
import kotlinx.coroutines.flow.Flow

class EpisodeViewModel(private val appDatabase: AppDatabase): ViewModel() {
    val listData = Pager(PagingConfig(pageSize = 1)) {
        RickyMortyPagingSource<Episode>(AppModule.provideRetrofitInstance(), Constants.EPISODE, appDatabase)

    }.flow.cachedIn(viewModelScope)

    val localData = appDatabase.episodeDao().getAllEpisodes()

    fun searchEpisode(filter: Filter): Flow<PagingData<Episode>> {
        return Pager(PagingConfig(pageSize = 1)) {
            RickyMortyPagingSource<Episode>(
                AppModule.provideRetrofitInstance(),
                Constants.EPISODE,
                appDatabase,
                filter
            )
        }.flow.cachedIn(viewModelScope)
    }

    fun searchEpisodeInCache(name: String): Flow<List<EpisodeEntity>> {
        return appDatabase.episodeDao().getEpisodesByName(name)
    }

}

class EpisodeViewModelFactory(private val appDatabase: AppDatabase) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EpisodeViewModel::class.java)) {
            return EpisodeViewModel(appDatabase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}