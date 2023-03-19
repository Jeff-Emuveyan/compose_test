package com.example.composetest.hilt

import com.example.composetest.data.remote.Api
import com.example.composetest.model.UserResponse
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Inject

class ApiImpl @Inject constructor(private val retrofit: Retrofit): Api {

    override suspend fun getUsers(id: Int, noofRecords: Int): List<UserResponse> {
        return retrofit.create(Api::class.java).getUsers(id, noofRecords)
    }
}

@Module
@InstallIn(SingletonComponent::class)
abstract class ApiModule {

    @Binds
    abstract fun bindApi(apiImpl: ApiImpl): Api

}