package com.example.finflow.mood

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class LabelRepository {
    private val user = FirebaseAuth.getInstance().currentUser?.uid ?: "default"
    private val TAG = "Label Repository"
    private val db = FirebaseFirestore.getInstance()
    private val collection = db.collection("user").document(user)
        .collection("moodLabels")

    suspend fun getAllLabelsFromDb(): Flow<List<LabelEntity>> = flow{
        val snapshot = collection.get().await()
        val labels = snapshot.toObjects(LabelEntity::class.java)
        Log.i(TAG, "Labels: $labels")
        emit(labels)
    }

    suspend fun insertALabel(label: LabelEntity){
        collection.document(label.id.toString()).set(label).await()
    }

    suspend fun updateALabel(label: LabelEntity){
        collection.document(label.id.toString()).update(
            mapOf("name" to label.name
            ,"id" to label.id
            ,"intensity" to label.intensity, "freq" to label.freq)
        ).await()
    }

    suspend fun deleteALabel(label: LabelEntity){
        collection.document(label.id.toString()).delete().await()
    }
}