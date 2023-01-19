package com.redgunner.droidsoft.paging
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.redgunner.droidsoft.models.post.Post
import com.redgunner.droidsoft.network.WordpressApi
import retrofit2.HttpException
import java.io.IOException

private const val WORDPRESS_STARTING_PAGE_INDEX = 1

class RecentPostsPagingSource(
    private val wordpressApi: WordpressApi,
    private val nb: Int
    ) : PagingSource<Int, Post>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Post> {
        return try {
            val position = params.key ?: 1

            val posts = wordpressApi.getRecentPosts(
                page = position,
                perPage = 10,
                //nb = nb
            )

            LoadResult.Page(
                data = posts,
                prevKey = if (position == WORDPRESS_STARTING_PAGE_INDEX) null else position - 1,
                nextKey = if (posts.isEmpty()) null else position + 1
            )

        } catch (exception: IOException) {
            LoadResult.Error(exception)
        } catch (exception: HttpException) {

            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Post>): Int? {
        return state.anchorPosition?.let { state.closestItemToPosition(it)?.id }
    }
}