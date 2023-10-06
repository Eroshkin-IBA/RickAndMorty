package com.example.rickandmorty.character

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.rickandmorty.Constants
import com.example.rickandmorty.network.AppModule
import com.example.rickandmorty.network.response.Character
import com.example.rickandmorty.paging.RickyMortyPagingSource


class CharacterViewModel
 : ViewModel() {

    val listData = Pager(PagingConfig(pageSize = 1)) {
        RickyMortyPagingSource<Character>(AppModule.provideRetrofitInstance(), Constants.CHARACTER)

    }.flow.cachedIn(viewModelScope)

}