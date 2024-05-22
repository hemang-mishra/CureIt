package com.example.finflow.domain.usecases

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FeedbackRepo(private val label: String) {
    private val user = FirebaseAuth.getInstance().currentUser?.uid ?: "default"
    private val TAG = "JournalRemoteDataSource"
    private val db = FirebaseFirestore.getInstance()
    private val collection = db.collection("user").document(user).collection("Feedbacks").document("Labels").collection(label)

    fun insertFeedback(feedback: FeedbackEntity) {
        collection.document("${feedback.importance} ${feedback.date}").set(feedback)
    }

    suspend fun getFeedbacks(){
        val feedbacks = collection.get().await()
        val feedbackList = mutableListOf<FeedbackEntity>()
        for (document in feedbacks) {
            feedbackList.add(
                FeedbackEntity(
                    date = document.getString("date") ?: "",
                    id = document.getLong("id")?.toInt() ?: 0,
                    description = document.getString("description") ?: "",
                    importance = document.getLong("importance")?.toInt() ?: 0,
                    label = document.getString("label") ?: ""
                )
            )
        }

    }
}

data class FeedbackEntity(
    val date: String,
    val id: Int,
    val description: String,
    val importance: Int,
    val label: String
)