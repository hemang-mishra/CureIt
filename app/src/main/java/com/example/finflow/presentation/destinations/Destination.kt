package com.example.finflow.presentation.destinations

sealed class Destinations(val route: String) {
    object attentionList: Destinations("attentionList")
    object feedbackForm: Destinations("feedbackForm")
}