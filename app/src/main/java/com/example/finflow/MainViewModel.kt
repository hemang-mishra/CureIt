package com.example.finflow

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finflow.domain.usecases.DashboardRepo
import com.example.finflow.domain.usecases.DetailsEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch

class MainViewModel(): ViewModel() {
    private val repository = DashboardRepo()
    var details: Flow<DetailsEntity?> = flowOf(null)

    init {
        getDetails()
    }

    private fun getDetails() = viewModelScope.launch {
        details = repository.getDetails()
    }
}