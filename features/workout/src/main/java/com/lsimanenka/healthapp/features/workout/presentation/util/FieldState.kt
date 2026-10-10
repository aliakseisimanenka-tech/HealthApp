package com.lsimanenka.healthapp.features.workout.presentation.util

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FieldState(
    val value: String,
    val isError: Boolean = false
) : Parcelable