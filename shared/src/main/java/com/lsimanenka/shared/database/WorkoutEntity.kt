package com.lsimanenka.shared.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "workouts_table")
data class WorkoutEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val action: String,
    val date: Long,
    val duration: Int,  // minutes
    val cardio: Int,
    val distance: Int,  // meters
    val kcal: Int,
    val steps: Int,
    @ColumnInfo(defaultValue = "", name = "comment")
    val notes: String
)