package com.redgunner.droidsoft.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.redgunner.droidsoft.network.WordpressApi
import com.redgunner.droidsoft.paging.PostPagingSource
import com.redgunner.droidsoft.paging.RecentPostsPagingSource
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class WordPressRepository @Inject constructor(private val wordpressApi: WordpressApi) {

    fun getPostByCategory(CategoryId: Int) = Pager(
        config = PagingConfig(
            pageSize = 10,
            maxSize = 100,
            enablePlaceholders = false
        ),
        pagingSourceFactory = {
            PostPagingSource(
                categoryId = CategoryId,
                wordpressApi = wordpressApi
            )
        }
    ).liveData

    fun getRecentPosts(Nb: Int) = Pager(
        config = PagingConfig(
            pageSize = 10,
            maxSize = 100,
            enablePlaceholders = false
        ),
        pagingSourceFactory = {
            RecentPostsPagingSource(
                wordpressApi = wordpressApi,
                nb = Nb
            )
        }
    ).liveData

    suspend fun getCategories() = wordpressApi.getCategories()


    suspend fun getPostById(postId: Int) = wordpressApi.getPostById(postId = postId)

    suspend fun getPostComments(postId: Int) = wordpressApi.getPostComments(postId = postId)

    suspend fun postComment(postId: Int, authorName: String, authorEmail: String, content: String) = wordpressApi.postComment(postId = postId, authorName = authorName, authorEmail = authorEmail, content = content)

}