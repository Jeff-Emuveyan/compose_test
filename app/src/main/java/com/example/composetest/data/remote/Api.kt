package com.example.composetest.data.remote
import com.example.composetest.model.UserResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {
    @GET("/employee")
    suspend fun getUsers(@Query("idStarts") id: Int, @Query("noofRecords") noofRecords: Int): List<UserResponse>
}