package sv.edu.udb.investigacion02

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adaptador: AdaptadorPost
    private lateinit var progressBar: ProgressBar
    private lateinit var progressBarPost: ProgressBar
    private val apiService: ApiService by lazy { crearApiService() }
    private var posts: List<Post> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        progressBar = findViewById(R.id.progressBar)

        // UI para crear un nuevo post
        val tituloPost: EditText = findViewById(R.id.tituloPost)
        val cuerpoPost: EditText = findViewById(R.id.cuerpoPost)
        val btnCrearPost: Button = findViewById(R.id.btnCrearPost)
        progressBarPost = findViewById(R.id.progressBarPost)

        val btnRefresh: View = findViewById(R.id.btnRefresh)
        btnRefresh.setOnClickListener {
            Toast.makeText(this@MainActivity, "Refrescando Posts...", Toast.LENGTH_LONG).show()
            cargarPosts()
        }
        cargarPosts()

        btnCrearPost.setOnClickListener {
            val titulo = tituloPost.text.toString()
            val cuerpo = cuerpoPost.text.toString()

            if (titulo.isNotEmpty() && cuerpo.isNotEmpty()) {
                progressBarPost.visibility = View.VISIBLE
                crearPost(1, titulo, cuerpo)
            } else {
                Toast.makeText(this, "Debe ingresar el título y el cuerpo", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun cargarPosts() {
        progressBar.visibility = View.VISIBLE
        CoroutineScope(Dispatchers.Main).launch {
            try {
                posts = apiService.getPosts()
                adaptador = AdaptadorPost(posts)
                recyclerView.adapter = adaptador
            } catch (e: Exception) {
                Toast.makeText(this@MainActivity, "No se pudieron cargar los Post: ${e.message}", Toast.LENGTH_LONG).show()
            } finally {
                progressBar.visibility = View.GONE
            }
        }
    }

    private fun crearPost(userId: Int, title: String, body: String) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val nuevoPost = Post(userId, 0, title, body)
                Log.d("MainActivity", "Intentando crear un nuevo post: $nuevoPost")
                val postCreado = apiService.createPost(nuevoPost)
                Log.d("MainActivity", "Post creado: $postCreado")
                Toast.makeText(this@MainActivity, "Post creado con ID: ${postCreado.id}", Toast.LENGTH_LONG).show()
                cargarPosts()
            } catch (e: IOException) {
                Log.e("MainActivity", "Error de conexión: ${e.message}")
                Toast.makeText(this@MainActivity, "Error de conexión. Verifica tu conexión a Internet.", Toast.LENGTH_LONG).show()
            } catch (e: Exception) {
                Log.e("MainActivity", "Error al crear el Post: ${e.message}")
                Toast.makeText(this@MainActivity, "Error al crear el Post: ${e.message}", Toast.LENGTH_LONG).show()
            } finally {
                progressBarPost.visibility = View.GONE
            }
        }
    }

    private fun crearApiService(): ApiService {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://jsonplaceholder.typicode.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(ApiService::class.java)
    }
}
