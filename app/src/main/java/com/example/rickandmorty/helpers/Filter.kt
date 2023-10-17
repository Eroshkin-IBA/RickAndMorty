package com.example.rickandmorty.helpers

data class Filter(
    var name: String = "",
    var status: String = "",
    var gender: String = "",
    var species: String = "",
) {
    fun resetFilter() {
        status = ""
        gender = ""
        species = ""
    }

    fun isAnyFieldFilled(): Boolean {
        return name.isNotEmpty() || status.isNotEmpty() || gender.isNotEmpty() || species.isNotEmpty()
    }
}