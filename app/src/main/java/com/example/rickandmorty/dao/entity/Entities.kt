package com.example.rickandmorty.dao.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation

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
data class OriginEntity(
    @ColumnInfo(name = "origin_name")
    val name: String,
    @ColumnInfo(name = "origin_url")
    val url: String
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}

@Entity
data class EpisodeEntity(
    val air_date: String,
    val characters: String,
    val created: String,
    val episode: String,
    @PrimaryKey val id: Int,
    val name: String,
    val url: String,

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