package com.bigschack77.zebratc21sopapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bigschack77.zebratc21sopapp.data.model.Sop
import com.bigschack77.zebratc21sopapp.data.repository.SopRepository
import kotlinx.coroutines.flow.StateFlow

class SopViewModel(private val repository: SopRepository) : ViewModel() {
    val sops: StateFlow<List<Sop>> = repository.sops

    fun createSop(title: String): String = repository.createSop(title)

    fun updateSopTitle(sopId: String, title: String) {
        repository.updateSopTitle(sopId, title)
    }

    fun saveStep(
        sopId: String,
        stepId: String?,
        title: String,
        description: String,
        imagePaths: List<String>,
    ) {
        repository.upsertStep(
            sopId = sopId,
            stepId = stepId,
            title = title,
            description = description,
            imagePaths = imagePaths,
        )
    }

    companion object {
        fun factory(repository: SopRepository): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T =
                SopViewModel(repository) as T
        }
    }
}
