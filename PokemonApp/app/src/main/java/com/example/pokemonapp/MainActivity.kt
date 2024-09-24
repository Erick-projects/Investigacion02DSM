package com.example.pokemonapp

import android.os.Bundle
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import retrofit2.HttpException
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private val pokemonRepository = PokemonRepository() // Instancia del repositorio
    private lateinit var pokemonAdapter: PokemonAdapter // Adapter para el RecyclerView
    private lateinit var recyclerView: RecyclerView // RecyclerView para mostrar Pokémon
    private lateinit var searchView: SearchView // SearchView para buscar Pokémon
    private var allPokemons: List<Pokemon> = emptyList() // Lista de todos los Pokémon

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inicializa el RecyclerView
        recyclerView = findViewById(R.id.recyclerViewPokemons)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Inicializa el Adapter con una lista vacía
        pokemonAdapter = PokemonAdapter(emptyList())
        recyclerView.adapter = pokemonAdapter

        // Inicializa el SearchView
        searchView = findViewById(R.id.searchView)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterPokemons(newText)
                return true
            }
        })

        // Llamada para obtener la lista de Pokémon al iniciar la actividad
        fetchPokemons()
    }

    // Método para obtener la lista de Pokémon
    private fun fetchPokemons() {
        lifecycleScope.launch {
            try {
                // Llama al repositorio para obtener la lista de Pokémon
                val response: Response<PokemonResponse> = pokemonRepository.getPokemons()

                // Verifica si la respuesta fue exitosa
                if (response.isSuccessful) {
                    response.body()?.let { pokemonResponse ->
                        // Manejar la lista de Pokémon obtenida
                        allPokemons = pokemonResponse.results.take(20) // Toma solo los primeros 10 Pokémon
                        pokemonAdapter.updatePokemons(allPokemons) // Actualiza el Adapter
                        showToast("Pokémons obtenidos: ${allPokemons.size} Pokémon(s) encontrados.")
                    }
                } else {
                    // Manejar errores de la respuesta
                    showToast("Error: ${response.message()}")
                }
            } catch (e: HttpException) {
                // Manejar la excepción HTTP
                showToast("HTTP Exception: ${e.message()}")
            } catch (e: Exception) {
                // Manejar otras excepciones
                showToast("Exception: ${e.message}")
            }
        }
    }

    // Método para filtrar la lista de Pokémon según la búsqueda
    private fun filterPokemons(query: String?) {
        val filteredPokemons = if (query.isNullOrEmpty()) {
            allPokemons
        } else {
            allPokemons.filter { it.name.contains(query, ignoreCase = true) }
        }
        pokemonAdapter.updatePokemons(filteredPokemons) // Actualiza el Adapter con la lista filtrada
    }

    // Método para mostrar mensajes cortos en la pantalla
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
