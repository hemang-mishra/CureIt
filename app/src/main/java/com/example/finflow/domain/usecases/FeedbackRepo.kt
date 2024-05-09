package com.example.finflow.domain.usecases

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class FeedbackRepo(private val label: String) {
    private val user = FirebaseAuth.getInstance().currentUser?.uid ?: "default"
    private val TAG = "JournalRemoteDataSource"
    private val db = FirebaseFirestore.getInstance()
    private val collection = db.collection("user").document(user).collection("Feedbacks").document("Labels").collection(label)

    fun insertFeedback(feedback: FeedbackEntity) {
        collection.document("${feedback.importance} ${feedback.date}").set(feedback)
    }
}

data class FeedbackEntity(
    val date: String,
    val id: Int,
    val description: String,
    val importance: Int,
    val label: String
)