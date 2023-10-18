package com.example.rickandmorty.helpers
data class Filter(
    var name: String = "",
    var status: String = "",
    var gender: String = "",
    var species: String = "",
    var season: String = "",
    var type: String = "",
    var dimension: String = ""
) {
    fun resetFilter() {
        status = ""
        gender = ""
        species = ""
        season = ""
        type = ""
        dimension = ""
    }
    fun isAnyFieldFilled(): Boolean {
        return name.isNotEmpty() ||
                status.isNotEmpty() ||
                gender.isNotEmpty() ||
                species.isNotEmpty() ||
                season.isNotEmpty() ||
                type.isNotEmpty() ||
                dimension.isNotEmpty()
    }
}