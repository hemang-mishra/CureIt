package com.example.finflow.domain.usecases

import com.example.finflow.room.EnjoyEntity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class EnjoyRepo {
    val user = FirebaseAuth.getInstance().currentUser?.uid ?: "default"
    val TAG = "EnjoyRepo"
    val db = FirebaseFirestore.getInstance()
    val collection = db.collection("user").document(user).collection("Enjoy")

    suspend fun getEnjoy(): Flow<List<EnjoyEntity>> = flow {
        val snapshot = collection.get().await()
        val enjoy = snapshot.toObjects(EnjoyEntity::class.java)
        emit(enjoy)
    }

    suspend fun addEnjoy(enjoy: EnjoyEntity) {
        collection.document(enjoy.id.toString()).set(enjoy).await()
    }

    suspend fun updateEnjoy(enjoy: EnjoyEntity) {
        collection.document(enjoy.id.toString()).update(
            mapOf(
                "id" to enjoy.id,
                "name" to enjoy.name,
                "desc" to enjoy.desc,
                "rate" to enjoy.rate,
                "freq" to enjoy.freq
            )
        ).await()
    }

    suspend fun deleteEnjoy(enjoy: EnjoyEntity) {
        collection.document(enjoy.id.toString()).delete().await()
    }
}