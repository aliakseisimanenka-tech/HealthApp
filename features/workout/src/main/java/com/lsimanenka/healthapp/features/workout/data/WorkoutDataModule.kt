package com.lsimanenka.healthapp.features.workout.data

import com.lsimanenka.healthapp.features.workout.domain.WorkoutRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class AddWorkoutDataModule {

    @Binds
    abstract fun bindAddWorkoutRepository(
        impl: WorkoutRepositoryImpl
    ): WorkoutRepository
}