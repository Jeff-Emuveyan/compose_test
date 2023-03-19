package com.example.composetest.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.composetest.data.remote.Api
import com.example.composetest.model.UserResponse
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class UsersPagingSource @Inject constructor (private val ioDispatchers: CoroutineContext,
                                             private val api: Api): PagingSource<Int, UserResponse>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, UserResponse> = withContext(ioDispatchers) {
        return@withContext try {
            // Start refresh at page 1 if undefined.
            val nextPage = params.key ?: 1
            val response = api.getUsers(nextPage, 20)
            LoadResult.Page(
                data = response,
                prevKey = null,
                nextKey = response.last().id ?: 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e.fillInStackTrace())
        }
    }

    override fun getRefreshKey(state: PagingState<Int, UserResponse>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override val keyReuseSupported: Boolean
        get() = true
}