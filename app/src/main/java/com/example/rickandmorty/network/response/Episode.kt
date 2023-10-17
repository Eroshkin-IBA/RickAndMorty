package com.example.rickandmorty.network.response

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable


data class Episode(
    val air_date: String,
    val characters: List<String>,
    val created: String,
    val episode: String,
    val id: Int,
    val name: String,
    val url: String
) : Serializable