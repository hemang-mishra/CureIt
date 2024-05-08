package com.example.finflow

sealed class Destinations(val route: String) {
    object attentionList: Destinations("attentionList")
    object feedbackForm: Destinations("feedbackForm")
}