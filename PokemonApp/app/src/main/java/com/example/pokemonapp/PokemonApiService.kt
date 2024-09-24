package com.example.pokemonapp

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Body

interface PokemonApiService {
    // Solicitud GET para obtener la lista de Pokémon
    @GET("pokemon")
    suspend fun getPokemons(): Response<PokemonResponse>

    // Método de ejemplo para una solicitud POST para crear un nuevo Pokémon
    @POST("pokemon/create") // Reemplaza "pokemon/create" con el endpoint real si lo tienes
    suspend fun createPokemon(@Body newPokemon: Pokemon): Response<PokemonCreationResponse>
}
