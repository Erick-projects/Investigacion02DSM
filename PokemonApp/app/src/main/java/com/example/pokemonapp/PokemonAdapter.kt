package com.example.pokemonapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PokemonAdapter(private var pokemons: List<Pokemon>) : RecyclerView.Adapter<PokemonAdapter.PokemonViewHolder>() {

    class PokemonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val pokemonName: TextView = itemView.findViewById(R.id.pokemon_name)

        fun bind(pokemon: Pokemon) {
            pokemonName.text = pokemon.name.capitalize() // Muestra el nombre del Pokémon
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_pokemon, parent, false)
        return PokemonViewHolder(view)
    }

    override fun onBindViewHolder(holder: PokemonViewHolder, position: Int) {
        holder.bind(pokemons[position])
    }

    override fun getItemCount(): Int = pokemons.size

    // Método para actualizar la lista de Pokémon
    fun updatePokemons(newPokemons: List<Pokemon>) {
        pokemons = newPokemons
        notifyDataSetChanged() // Notifica al adapter que los datos han cambiado
    }
}
