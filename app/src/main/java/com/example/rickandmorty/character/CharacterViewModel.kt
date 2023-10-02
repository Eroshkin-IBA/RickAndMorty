package com.example.rickandmorty.character

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.rickandmorty.network.ApiService
import com.example.rickandmorty.network.AppModule
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


class CharacterViewModel
 : ViewModel() {

    val listData = Pager(PagingConfig(pageSize = 1)) {
        RickyMortyPagingSource(AppModule.provideRetrofitInstance(AppModule.provideBaseUrl()))

    }.flow.cachedIn(viewModelScope)

}