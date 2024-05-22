package com.example.finflow.domain.usecases

import com.example.finflow.goals.GoalEntity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class GainRepository {
    val user = FirebaseAuth.getInstance().currentUser?.uid ?: "default"
    val TAG = "GainRepository"
    val db = FirebaseFirestore.getInstance()
    val collection = db.collection("user").document(user).collection("Gains")

    suspend fun getGains(): Flow<List<GoalEntity>> = flow {
        val snapshot = collection.get().await()
        val gains = snapshot.toObjects(GoalEntity::class.java)
        emit(gains)
    }

    suspend fun addGains(gain: GoalEntity) {
        collection.document(gain.id.toString()).set(gain).await()
    }

    suspend fun updateGains(gain: GoalEntity) {
        collection.document(gain.id.toString()).update(
            mapOf(
                "id" to gain.id,
                "domain" to gain.domain,
                "presentTimeSpent" to gain.presentTimeSpent,
                "rate" to gain.rate,
                "expectedPercent" to gain.expectedPercent,
                "presentPercent" to gain.presentPercent
            )
        ).await()
    }

    suspend fun deleteGains(gain: GoalEntity) {
        collection.document(gain.id.toString()).delete().await()
    }
}