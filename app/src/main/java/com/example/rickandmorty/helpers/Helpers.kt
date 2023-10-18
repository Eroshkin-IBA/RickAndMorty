package com.example.rickandmorty.helpers

import com.example.rickandmorty.dao.entity.CharacterEntity
import com.example.rickandmorty.dao.entity.EpisodeEntity
import com.example.rickandmorty.dao.entity.LocationEntity
import com.example.rickandmorty.network.response.Character
import com.example.rickandmorty.network.response.Episode
import com.example.rickandmorty.network.response.Location
import com.example.rickandmorty.network.response.Origin

fun extractIdFromUri(uri: String): Int {
    val lastSegment = uri.substringAfterLast("/")
    return lastSegment.toInt()
}

fun extractSeasonAndEpisode(input: String): Pair<String, String>? {
    val seasonPattern = Regex("S(\\d+)E(\\d+)")
    val matchResult = seasonPattern.find(input)
    return if (matchResult != null && matchResult.groupValues.size == 3) {
        val seasonNumber = matchResult.groupValues[1]
        val episodeNumber = matchResult.groupValues[2]
        Pair("Season: $seasonNumber", "Series $episodeNumber")
    } else {
        null
    }
}

fun convertCharacterListToEntityList(characters: List<Character>): List<CharacterEntity> {
    return characters.map { character ->
        CharacterEntity(
            characterId = character.id,
            name = character.name,
            status = character.status,
            species = character.species,
            type = character.type,
            gender = character.gender,
            location = character.location.name,
            locationUri = character.location.url,
            image = character.image,
            episodes = character.episode.joinToString(", "),
            url = character.url,
            created = character.created
        )
    }
}

fun convertToEpisodeEntities(episodes: List<Episode>): List<EpisodeEntity> {
    return episodes.map {
        EpisodeEntity(
            it.air_date,
            it.characters.joinToString(", "),
            it.created,
            it.episode,
            it.id,
            it.name,
            it.url
        )
    }
}

fun convertToLocationEntities(locations: List<Location>): List<LocationEntity> {
    return locations.map {
        LocationEntity(
            id = it.id,
            created = it.created,
            dimension = it.dimension,
            name = it.name,
            residents = it.residents.joinToString(", "),
            type = it.type,
            url = it.url
        )
    }
}

fun characterEntityToCharacter(characterEntity: CharacterEntity): Character {
    return Character(
        characterEntity.characterId,
        characterEntity.name,
        characterEntity.status,
        characterEntity.species,
        characterEntity.type,
        characterEntity.gender,
        Origin(),
        Location(name = characterEntity.location, url = characterEntity.locationUri),
        characterEntity.image,
        characterEntity.episodes.split(", "),
        characterEntity.url,
        characterEntity.created
    )
}

fun episodeEntityToEpisode(entity: EpisodeEntity): Episode {
    return Episode(
        air_date = entity.air_date,
        characters = entity.characters.split(", "),
        created = entity.created,
        episode = entity.episode,
        id = entity.id,
        name = entity.name,
        url = entity.url
    )
}

fun locationEntityToLocation(locationEntity: LocationEntity): Location {
    return Location(
        created = locationEntity.created,
        dimension = locationEntity.dimension,
        id = locationEntity.id,
        name = locationEntity.name,
        residents = locationEntity.residents.split(", "),
        type = locationEntity.type,
        url = locationEntity.url
    )
}

