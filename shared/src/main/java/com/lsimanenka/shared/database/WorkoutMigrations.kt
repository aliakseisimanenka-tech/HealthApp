package com.lsimanenka.shared.database

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

object WorkoutMigrations {

    val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL(
                "ALTER TABLE workouts_table ADD COLUMN comment TEXT NOT NULL DEFAULT ''"
            )
        }

    }

}