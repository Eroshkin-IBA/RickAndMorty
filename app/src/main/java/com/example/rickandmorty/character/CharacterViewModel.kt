package com.example.rickandmorty.character

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.rickandmorty.Constants
import com.example.rickandmorty.dao.AppDatabase
import com.example.rickandmorty.dao.entity.CharacterEntity
import com.example.rickandmorty.helpers.Filter
import com.example.rickandmorty.network.AppModule
import com.example.rickandmorty.network.response.Character
import com.example.rickandmorty.paging.RickyMortyPagingSource
import kotlinx.coroutines.flow.Flow

class CharacterViewModel(private val appDatabase: AppDatabase) : ViewModel() {
    val listData = Pager(PagingConfig(pageSize = 1)) {
        RickyMortyPagingSource<Character>(
            AppModule.provideRetrofitInstance(),
            Constants.CHARACTER,
            appDatabase
        )
    }.flow.cachedIn(viewModelScope)
    var localListData = appDatabase.characterDao().getAllCharacterEntities()
    fun searchCharacter(filter: Filter): Flow<PagingData<Character>> {
        return Pager(PagingConfig(pageSize = 1)) {
            RickyMortyPagingSource<Character>(
                AppModule.provideRetrofitInstance(),
                Constants.CHARACTER,
                appDatabase,
                filter
            )
        }.flow.cachedIn(viewModelScope)
    }

    fun searchCharacterInCache(filter: Filter): Flow<List<CharacterEntity>> {
        return appDatabase.characterDao()
            .getCharactersByFilter(filter.name, filter.gender, filter.status, filter.species)
    }
}

class CharacterViewModelFactory(private val appDatabase: AppDatabase) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CharacterViewModel::class.java)) {
            return CharacterViewModel(appDatabase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}