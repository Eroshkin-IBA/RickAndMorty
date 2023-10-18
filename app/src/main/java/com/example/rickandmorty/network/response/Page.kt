package com.example.rickandmorty.network.response

data class Page<T : Any>(
    val info: Info,
    val results: List<T>
)

data class Info(
    val count: Int,
    val pages: Int,
    val next: String?,
    val prev: String?
)