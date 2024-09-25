package sv.edu.udb.investigacion02

import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Body
import retrofit2.Call

interface ApiService {
    @GET("/posts")
    suspend fun getPosts(): List<Post>

    @POST("/posts")
    suspend fun createPost(@Body post: Post): Post
}