package com.example.finflow.mood

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class MoodHistoryRepository(date: String) {
    private val user = FirebaseAuth.getInstance().currentUser?.uid ?: "default"
    private val TAG = "MoodHistoryRepository"
    private val db = FirebaseFirestore.getInstance()
    private val collection = db.collection("user").document(user)
        .collection("moodHistoryDates").document(date).collection("moodHistory")
    private val collection_day = db.collection("user").document(user)
        .collection("moodHistoryDates")
//    val movieList = listOf<String>()
//    movieList.add("hi")

    suspend fun getAllMoodHistoryFromDb(): Flow<List<MoodHistoryEntity>> = flow {
        val snapshot = collection.get().await()
        val moodHistory = snapshot.toObjects(MoodHistoryEntity::class.java)
        Log.i(TAG, "MoodHistory: $moodHistory")
        emit(moodHistory)
    }

    private suspend fun insertAMoodHistory(moodHistory: MoodHistoryEntity) {
        collection.document(moodHistory.id.toString()).set(moodHistory).await()
    }

    suspend fun updateAMoodHistory(moodHistory: MoodHistoryEntity) {
        collection.document(moodHistory.id.toString()).update(
            mapOf(
                "id" to moodHistory.id,
                "avgMoodIntensity" to moodHistory.avgMoodIntensity,
                "date" to moodHistory.date,
                "time" to moodHistory.time,
                "thoughts" to moodHistory.thoughts,
                "moodLabels" to moodHistory.moodLabels,
                "change" to moodHistory.change
            )
        ).await()
    }

    suspend fun deleteAMoodHistory(moodHistory: MoodHistoryEntity) {
        collection.document(moodHistory.id.toString()).delete().await()
    }

    suspend fun insertWithUpdate(moodHistory: MoodHistoryEntity) {
        var events: List<MoodHistoryEntity> = emptyList()
        getAllMoodHistoryFromDb().collect {
            events = it
            var sum = 0.0F
            events.forEach { event ->
                sum += event.avgMoodIntensity
            }
            val avg = (sum + moodHistory.avgMoodIntensity) / (events.size + 1)
            updateAvg(avg, moodHistory.date)
            updateLastMood(moodHistory)
            insertAMoodHistory(moodHistory)
        }
    }

    suspend fun updateAvg(avg: Float, date: String) {
        collection_day.document(date).update(
            mapOf(
                "Average" to avg
            )
        ).addOnFailureListener {
            collection_day.document(date).set(
                mapOf(
                    "Average" to avg
                )
            )
        }.await()
    }

    suspend fun getAvg(date: String): Float {
        val snapshot = collection_day.document(date).get().await()
        val avg = snapshot.getDouble("Average")
        if (avg != null) {
            return avg.toFloat()
        } else
            return 0.0F
    }

    suspend fun updateLastMood(moodHistory: MoodHistoryEntity) {
        collection_day.document(moodHistory.date).update(
            mapOf(
                "Last Mood" to moodHistory.avgMoodIntensity
            )
        ).addOnFailureListener {
            collection_day.document(moodHistory.date).set(
                mapOf(
                    "Last Mood" to moodHistory.avgMoodIntensity
                )
            )
        }
            .await()

    }

    suspend fun getLastMood(date: String): Float {
        val snapshot = collection_day.document(date).get().await()
        val lastAvg = snapshot.getDouble("Last Mood")
        if (lastAvg != null) {
            return lastAvg.toFloat()
        }
        return 0.0F
    }

    suspend fun initialDateDocument() {
        collection_day.document("2021-01-01").set(
            mapOf(
                "Init" to 0
            )
        ).await()
    }
}