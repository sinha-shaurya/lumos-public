package com.example.lumos.network.adapters

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.lumos.network.api.BlogApi
import com.example.lumos.network.dataclasses.blog.BlogPost
import retrofit2.HttpException
import java.io.IOException

private const val BLOG_START_PAGE = 1

class BlogPagingSource(private val blogApi: BlogApi) : PagingSource<Int, BlogPost>() {
    override fun getRefreshKey(state: PagingState<Int, BlogPost>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, BlogPost> {
        //setup paging
        //take 1 as starting page if no key is available
        val position = params.key ?: BLOG_START_PAGE
        return try {
            val response = blogApi.getPost(position,params.loadSize)
            LoadResult.Page(
                data = response,
                prevKey = if (position == BLOG_START_PAGE) null else position - 1,
                nextKey = if (response.isEmpty()) null else position + 1
            )
        } catch (exception: IOException) {
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        }
    }
}