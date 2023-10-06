package com.example.rickandmorty.helpers

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