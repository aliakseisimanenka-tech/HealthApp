package com.lsimanenka.shared.remote

interface WorkoutSyncManager {
    suspend fun sync()
}