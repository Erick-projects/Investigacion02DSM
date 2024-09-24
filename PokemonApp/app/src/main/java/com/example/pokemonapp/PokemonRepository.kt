package com.example.pokemonapp

import retrofit2.Response

class PokemonRepository {
    private val apiService = RetrofitInstance.api

    // Método para obtener la lista de Pokémon
    suspend fun getPokemons(): Response<PokemonResponse> {
        return apiService.getPokemons()
    }
}
