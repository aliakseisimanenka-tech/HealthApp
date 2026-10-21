package com.lsimanenka.shared.remote

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface WorkoutApi {

    @POST("data/WorkoutRemote")
    suspend fun pushWorkout(@Body workout: WorkoutRemote)

    @GET("data/WorkoutRemote")
    suspend fun pullWorkouts(
        @Query("where") whereClause: String,
        @Query("pageSize") pageSize: Int = 100
    ): List<WorkoutRemote>
}