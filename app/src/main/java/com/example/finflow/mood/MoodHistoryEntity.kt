package com.example.finflow.mood

data class MoodHistoryEntity(
    var id: String = "",
    var avgMoodIntensity: Float = 0.0F,
    var date: String = "",
    var time: String = "",
    var thoughts: String = "",
    var moodLabels: MutableList<LabelEntity> = mutableListOf(),
    var change: String = ""
)
