package com.lsimanenka.shared.database

import android.content.Context
import androidx.room.Room
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DatabaseModule {

    @Binds
    @Singleton
    abstract fun bindsSharedStatsProvider(
        sharedStatsProviderImpl: SharedStatsProviderImpl
    ) : SharedStatsProvider

    companion object {
        @Provides
        @Singleton
        fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
            return Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                "health_app_database"
            )
                .addMigrations(WorkoutMigrations.MIGRATION_1_2, WorkoutMigrations.MIGRATION_2_3)
                .build()
        }

        @Provides
        fun provideWorkoutDao(database: AppDatabase): WorkoutDao {
            return database.workoutDao()
        }
    }
}