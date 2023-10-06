package com.example.rickandmorty.location

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.rickandmorty.Constants
import com.example.rickandmorty.network.AppModule
import com.example.rickandmorty.network.response.Character
import com.example.rickandmorty.network.response.Location
import com.example.rickandmorty.paging.RickyMortyPagingSource

class LocationViewModel : ViewModel() {

    val listData = Pager(PagingConfig(pageSize = 1)) {
        RickyMortyPagingSource<Location>(AppModule.provideRetrofitInstance(), Constants.LOCATION)

    }.flow.cachedIn(viewModelScope)
}