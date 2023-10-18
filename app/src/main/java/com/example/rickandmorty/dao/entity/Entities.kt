package com.example.rickandmorty.dao.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CharacterEntity(
    @PrimaryKey val characterId: Int,
    val name: String,
    val status: String,
    val species: String,
    val type: String,
    val gender: String,
    val location: String,
    val locationUri: String,
    val image: String,
    val episodes: String,
    val url: String,
    val created: String
)

@Entity
data class EpisodeEntity(
    val air_date: String,
    val characters: String,
    val created: String,
    val episode: String,
    @PrimaryKey val id: Int,
    val name: String,
    val url: String
)

@Entity
data class LocationEntity(
    @PrimaryKey val id: Int,
    val created: String,
    val dimension: String,
    val name: String,
    val residents: String,
    val type: String,
    val url: String
)