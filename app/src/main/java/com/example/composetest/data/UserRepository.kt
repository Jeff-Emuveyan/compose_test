package com.example.composetest.data

import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.RemoteMediator
import com.example.composetest.data.local.AppDatabase
import com.example.composetest.data.remote.Api
import com.example.composetest.model.UserResponse
import com.example.composetest.paging.UserRemoteMediator
import com.example.composetest.paging.UsersPagingSource
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class UserRepository @Inject constructor(val ioDispatchers: CoroutineContext,
                                         val api: Api,
                                         val appDatabase: AppDatabase,
                                         private val pagingSource: UsersPagingSource,
                                         private val remoteMediator: UserRemoteMediator) {

   /* suspend fun getUser(id: Int, noofRecords: Int): List<UserResponse>? = withContext(ioDispatchers) {
        try {
            api.getUsers(id, noofRecords)
        } catch (e: Exception) {
            null
        }
    }*/

    /***
     * GET LIST OF PAGINATED USERS
     */
    fun getUsers() = Pager(PagingConfig(pageSize = 20)) { pagingSource }

    /**
     * GET LIST OF PAGINATED USERS WITH LOCAL STORAGE BACKUP
     */
    @OptIn(ExperimentalPagingApi::class)
    fun getUsersWithLocalPagination(id: Int) = Pager(
        config = PagingConfig(pageSize = 20),
        remoteMediator = remoteMediator
    ) {
        appDatabase.userDao().pagingSource(id)
    }
}
