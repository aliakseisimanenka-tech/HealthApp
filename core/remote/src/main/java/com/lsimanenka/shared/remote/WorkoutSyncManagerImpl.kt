package com.lsimanenka.shared.remote

import com.lsimanenka.shared.database.WorkoutDao
import com.lsimanenka.shared.database.WorkoutEntity
import com.lsimanenka.shared.datastore.TokenManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WorkoutSyncManagerImpl @Inject constructor(
    private val workoutDao: WorkoutDao,
    private val tokenManager: TokenManager,
    private val workoutApi: WorkoutApi
) : WorkoutSyncManager {
    override suspend fun sync() = withContext(Dispatchers.IO) {
        val userId = tokenManager.userIdFlow.first() ?: return@withContext

        try {
            pushUnsyncedWorkouts(userId)
            pullRemoteWorkouts(userId)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private suspend fun pushUnsyncedWorkouts(userId: String) {
        val unsynced = workoutDao.getUnsyncedWorkouts()
        if (unsynced.isEmpty()) return

        for (local in unsynced) {
            val remote = WorkoutRemote(
                ownerId = userId,
                name = local.name,
                action = local.action,
                date = local.date,
                duration = local.duration,
                cardio = local.cardio,
                distance = local.distance,
                kcal = local.kcal,
                steps = local.steps,
                notes = local.notes
            )

            workoutApi.pushWorkout(remote)

            workoutDao.insertWorkout(local.copy(isSynced = true))
        }
    }

    private suspend fun pullRemoteWorkouts(userId: String) {
        val whereClause = "ownerId = '$userId'"

        val remoteWorkouts = workoutApi.pullWorkouts(whereClause = whereClause)

        if (remoteWorkouts.isNotEmpty()) {
            workoutDao.clearSyncedUserWorkouts(userId)

            val entities = remoteWorkouts.map { remote ->
                WorkoutEntity(
                    id = 0,
                    name = remote.name,
                    action = remote.action,
                    date = remote.date,
                    duration = remote.duration,
                    cardio = remote.cardio,
                    distance = remote.distance,
                    kcal = remote.kcal,
                    steps = remote.steps,
                    notes = remote.notes,
                    ownerId = userId,
                    isSynced = true,
                    isDeleted = false
                )
            }
            workoutDao.insertWorkouts(entities)
        }
    }
}