package com.example.rickandmorty.network.response

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

data class Location(
    val created: String = "",
    val dimension: String = "",
    val id: Int = 0,
    val name: String = "",
    val residents: List<String> = listOf(),
    val type: String = "",
    val url: String = ""
) : Serializable
