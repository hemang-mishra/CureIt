package com.example.finflow

import com.example.finflow.goals.GoalEntity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class GoalRemoteRepo {
    private val user = FirebaseAuth.getInstance().currentUser?.uid ?: "default"
    private val TAG = "Goal Remote"
    private val db = FirebaseFirestore.getInstance()
    private val collection = db.collection("user").document(user)
        .collection("goals")


    suspend fun replaceAllGoals(goals: List<GoalEntity>){
        collection.get().addOnSuccessListener {
            for (document in it.documents){
                document.reference.delete()
            }
        }
        for (goal in goals){
            collection.document(goal.id.toString()).set(goal)
        }
    }

    suspend fun getAllGoalsFromDb(): List<GoalEntity> {
        val snapshot = collection.get().await()
        val goals = snapshot.toObjects(GoalEntity::class.java)
        return goals
    }
}