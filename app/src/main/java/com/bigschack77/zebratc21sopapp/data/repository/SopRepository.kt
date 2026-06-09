package com.bigschack77.zebratc21sopapp.data.repository

import com.bigschack77.zebratc21sopapp.data.model.Sop
import kotlinx.coroutines.flow.StateFlow

interface SopRepository {
    val sops: StateFlow<List<Sop>>

    fun createSop(title: String): String

    fun updateSopTitle(sopId: String, title: String)

    fun upsertStep(
        sopId: String,
        stepId: String?,
        title: String,
        description: String,
        imagePaths: List<String>,
    )
}
