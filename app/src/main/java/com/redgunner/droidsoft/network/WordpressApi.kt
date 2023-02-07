package com.redgunner.droidsoft.network

import com.redgunner.droidsoft.models.category.Categories
import com.redgunner.droidsoft.models.comments.Comments
import com.redgunner.droidsoft.models.post.Post
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Headers

interface WordpressApi {

    companion object {
        const val BASE_URL = "https://droidsoft.fr/"


    }


    @GET("/wp-json/wp/v2/posts")
    suspend fun getPostsByCategories(
        @Query("categories") categories: Int,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
        @Query("_embed") embed: Boolean
    ): List<Post>

    @GET("/wp-json/wp/v2/posts?_embed=true")
    suspend fun getRecentPosts(
        @Query("per_page") perPage: Int,
        @Query("page") page: Int,
    ): List<Post>

    @GET("/wp-json/wp/v2/categories?per_page=50")
    suspend fun getCategories(): List<Categories>


    @GET("/wp-json/wp/v2/posts/{postId}?&_embed=true")
    suspend fun getPostById(
        @Path("postId") postId: Int
    ): Post


    @GET("/wp-json/wp/v2/comments")
    suspend fun getPostComments(
        @Query("post") postId: Int
    ): List<Comments>

    @Headers("Authorization:    ")
    @POST("/wp-json/wp/v2/comments")
    suspend fun postComment(
        @Query("post") postId: Int,
        @Query("author_name") authorName: String,
        @Query("author_email") authorEmail: String,
        @Query("content") content: String
    ): Comments

}