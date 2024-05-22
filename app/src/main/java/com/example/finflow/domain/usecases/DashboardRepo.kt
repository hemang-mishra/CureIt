package com.example.finflow.domain.usecases

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class DashboardRepo {
    private val user = FirebaseAuth.getInstance().currentUser?.uid ?: "default"
    private val TAG = "Dashboard Repo"
    private val db = FirebaseFirestore.getInstance()
    private val collection = db.collection("user").document(user).collection("Details")

    suspend fun getDetails(): Flow<DetailsEntity?> = flow {
        val snapshot = collection.document("Details").get().await()
        val details = snapshot.toObject(DetailsEntity::class.java)
        emit(details)
    }

    suspend fun updateDetails(details: DetailsEntity) {
        collection.document("Details").set(details).await()
    }

}

data class DetailsEntity(
    val score: Long = 0,
    val level: Int = 0
)