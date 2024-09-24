package com.example.pokemonapp

data class PokemonResponse(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<Pokemon>
)

data class Pokemon(
    val name: String,
    val url: String
)

data class PokemonCreationResponse(
    val id: Int,
    val name: String,
    val message: String
)
