package com.example.rickandmorty.location

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
import com.example.rickandmorty.dao.entity.LocationEntity
import com.example.rickandmorty.helpers.Filter
import com.example.rickandmorty.network.AppModule
import com.example.rickandmorty.network.response.Character
import com.example.rickandmorty.network.response.Location
import com.example.rickandmorty.paging.RickyMortyPagingSource
import kotlinx.coroutines.flow.Flow

class LocationViewModel(private val appDatabase: AppDatabase) : ViewModel() {

    val listData = Pager(PagingConfig(pageSize = 1)) {
        RickyMortyPagingSource<Location>(
            AppModule.provideRetrofitInstance(),
            Constants.LOCATION,
            appDatabase
        )

    }.flow.cachedIn(viewModelScope)

    val localData = appDatabase.locationDao().getAllLocations()

    fun searchLocation(filter: Filter): Flow<PagingData<Location>> {
        return Pager(PagingConfig(pageSize = 1)) {
            RickyMortyPagingSource<Location>(
                AppModule.provideRetrofitInstance(),
                Constants.LOCATION,
                appDatabase,
                filter
            )
        }.flow.cachedIn(viewModelScope)
    }

    fun searchLocationInCache(name: String): Flow<List<LocationEntity>> {
        return appDatabase.locationDao().getLocationByName(name)
    }

}

class LocationViewModelFactory(private val appDatabase: AppDatabase) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LocationViewModel::class.java)) {
            return LocationViewModel(appDatabase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
