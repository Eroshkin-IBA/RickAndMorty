package com.example.rickandmorty.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.rickandmorty.dao.entity.CharacterEntity
import com.example.rickandmorty.dao.entity.EpisodeEntity
import com.example.rickandmorty.dao.entity.LocationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CharacterDao {
    @Query(
        "SELECT * FROM characterEntity WHERE name LIKE '%' || :name || '%' " +
                "AND gender LIKE :gender " +
                "AND status LIKE '%' || :status || '%' " +
                "AND species LIKE '%' || :species || '%'"
    )
    fun getCharactersByFilter(
        name: String,
        gender: String,
        status: String,
        species: String
    ): Flow<List<CharacterEntity>>

    @Query("SELECT * FROM characterEntity")
    fun getAllCharacterEntities(): Flow<List<CharacterEntity>>

    @Query("SELECT * FROM characterEntity WHERE characterId = :characterId")
    fun getCharacterById(characterId: Int): CharacterEntity

    @Query("SELECT * FROM characterEntity WHERE url = :url")
    fun getCharacterByUrl(url: String): CharacterEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCharacter(character: CharacterEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(characters: List<CharacterEntity>)
}

@Dao
interface EpisodeDao {
    @Query(
        "SELECT * FROM episodeEntity WHERE name LIKE '%' || :name || '%'" +
                "AND episode LIKE '%' || :season || '%'"
    )
    fun getEpisodesByFilter(name: String, season: String): Flow<List<EpisodeEntity>>

    @Query("SELECT * FROM episodeEntity")
    fun getAllEpisodes(): Flow<List<EpisodeEntity>>

    @Query("SELECT * FROM episodeEntity WHERE id = :id")
    fun getEpisodeById(id: Int): EpisodeEntity

    @Query("SELECT * FROM EpisodeEntity WHERE url = :url")
    fun getEpisodeByUrl(url: String): EpisodeEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertEpisode(episode: EpisodeEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(episode: List<EpisodeEntity>)
}

@Dao
interface LocationDao {
    @Query(
        "SELECT * FROM locationEntity WHERE name LIKE '%' || :name || '%'" +
                "AND type LIKE '%' || :type || '%' AND dimension LIKE '%' || :dimension || '%' "
    )
    fun getLocationByFilter(
        name: String,
        type: String,
        dimension: String
    ): Flow<List<LocationEntity>>

    @Query("SELECT * FROM locationEntity WHERE id = :id")
    fun getLocationById(id: Int): LocationEntity

    @Query("SELECT * FROM LocationEntity")
    fun getAllLocations(): Flow<List<LocationEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertLocation(location: LocationEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(location: List<LocationEntity>)
}