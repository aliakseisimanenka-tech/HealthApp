package com.lsimanenka.shared.remote

data class WorkoutRemote(
    var objectId: String? = null,
    var ownerId: String? = null,
    var name: String = "",
    var action: String = "",
    var date: Long = 0,
    var duration: Int = 0,
    var cardio: Int = 0,
    var distance: Int = 0,
    var kcal: Int = 0,
    var steps: Int = 0,
    var notes: String = ""
) {
    constructor() : this(null, null, "", "", 0, 0, 0, 0, 0, 0, "")
}