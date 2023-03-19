package com.example.composetest.paging

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.example.composetest.data.local.AppDatabase
import com.example.composetest.data.remote.Api
import com.example.composetest.model.User
import com.example.composetest.model.UserResponse
import com.example.composetest.model.local.UserEntity
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@OptIn(ExperimentalPagingApi::class)
class UserRemoteMediator @Inject constructor(
     val api: Api,
     val database: AppDatabase,
     val ioDispatchers: CoroutineContext): RemoteMediator<Int, UserEntity>() {

    override suspend fun load(loadType: LoadType, state: PagingState<Int, UserEntity>): MediatorResult {

        // When the paging library run for the first time, the value of loadType will be REFRESH.
        // This signifies the initial loading of the first set of data.
        // This will only happen once and then the value will change to APPEND as the user scrolls down the list.

        return try {
            val lastUserId = when (loadType) {
                LoadType.REFRESH -> {
                    // When loadType is REFRESH it means the library wants to load data for the first time.
                    // In this case, we want to start loading with the user id = 1
                    1
                }
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true) // END PAGINATION
                LoadType.APPEND -> {
                    // APPEND is the second action to be performed by the pagination library after REFRESH action.
                    // The value of loadType will be == APPEND each time the user scrolls towards the end of the list.
                    // Now, the first thing we need to do in this case is to get the id of the last item that was gotten from the
                    // network and use it to make the next network call.
                    // If lastItem is null it means no items were loaded after the initial REFRESH and there are
                    // no more items to load.
                    // Read more here: https://developer.android.com/topic/libraries/architecture/paging/v3-network-db#item-keys
                    val lastItem = state.lastItemOrNull()
                        ?: return MediatorResult.Success(endOfPaginationReached = true)

                    lastItem.id
                }
            }

            val response = api.getUsers(lastUserId, 20)

            withContext(ioDispatchers) {
                val userDao = database.userDao()

                if (loadType == LoadType.REFRESH) {
                    userDao.clearAll()
                }
                userDao.insertAll(convertResponse(response))
            }

            // Now, we need to decide if there are more data to load or if the library should end the pagination process.
            // In our case, if the list response is empty, it means we have finished loading all data from the backend. So we do:
            val endOfPageReached = response.isEmpty()
            MediatorResult.Success(endOfPaginationReached = endOfPageReached)

            //NOTE: If endOfPageReached is false, the library will call this load() method once again
            // and loadType will be == APPEND. But if endOfPageReached == true, the library will stop pagination.

        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        }
    }

    private fun convertResponse(list: List<UserResponse>): List<UserEntity> {
        val result: MutableList<UserEntity> = mutableListOf()
        list.forEach { result.add(it.toUserEntity()) }
        return result
    }

}