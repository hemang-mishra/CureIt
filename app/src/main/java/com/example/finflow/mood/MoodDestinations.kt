package com.example.finflow.mood

sealed class MoodDestinations(val route: String) {

    object labelList: MoodDestinations("labelList")
    object addLabel: MoodDestinations("addLabel")
    object editLabel: MoodDestinations("editLabel")
    object moodHistory: MoodDestinations("moodHistory")

    object success: MoodDestinations("success")
}