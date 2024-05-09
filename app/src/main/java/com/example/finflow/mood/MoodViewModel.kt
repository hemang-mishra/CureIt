package com.example.finflow.mood

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class MoodViewModel: ViewModel(){
    val labelRepository = LabelRepository()
    val labelsList: Flow<List<LabelEntity>>? = null

    suspend fun getAllLabelsFromRepo() = labelRepository.getAllLabelsFromDb()

    suspend fun getAllLabels(): Flow<List<LabelEntity>> {
        if(labelsList == null)
            return getAllLabelsFromRepo()
        return labelsList
    }

    fun insertALabel(label: LabelEntity) {
        viewModelScope.launch {
            labelRepository.insertALabel(label)
        }
    }

    fun updateALabel(label: LabelEntity) {
        viewModelScope.launch {
            labelRepository.updateALabel(label)
        }
    }

    fun insertAMoodHistory(moodHistory: MoodHistoryEntity) {
        viewModelScope.launch {
            MoodHistoryRepository(moodHistory.date).insertWithUpdate(moodHistory)
        }
    }

    suspend fun getAvg(date: String) = MoodHistoryRepository(date).getAvg(date)


    suspend fun getLastAvg(date: String) = MoodHistoryRepository(date).getLastMood(date)

    suspend fun initDateDocuments(date: String){
        MoodHistoryRepository(date).initialDateDocument()
    }
}