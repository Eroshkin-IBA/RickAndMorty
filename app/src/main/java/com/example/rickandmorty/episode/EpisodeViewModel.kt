package com.example.rickandmorty.episode

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.rickandmorty.Constants
import com.example.rickandmorty.network.AppModule
import com.example.rickandmorty.network.response.Character
import com.example.rickandmorty.network.response.Episode
import com.example.rickandmorty.paging.RickyMortyPagingSource

class EpisodeViewModel: ViewModel() {
    val listData = Pager(PagingConfig(pageSize = 1)) {
        RickyMortyPagingSource<Episode>(AppModule.provideRetrofitInstance(), Constants.EPISODE)

    }.flow.cachedIn(viewModelScope)
}