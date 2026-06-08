package com.bigschack77.zebratc21sopapp.data.repository

import com.bigschack77.zebratc21sopapp.data.model.Sop
import com.bigschack77.zebratc21sopapp.data.model.Step
import com.bigschack77.zebratc21sopapp.data.model.StepImage
import java.time.LocalDateTime
import java.util.UUID
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class InMemorySopRepository : SopRepository {
    private val sopState = MutableStateFlow<List<Sop>>(emptyList())

    override val sops: StateFlow<List<Sop>> = sopState.asStateFlow()

    override fun createSop(title: String): String {
        val now = LocalDateTime.now()
        val sop = Sop(
            id = UUID.randomUUID().toString(),
            title = title.ifBlank { "Ny SOP" },
            createdAt = now,
            updatedAt = now,
        )
        sopState.update { listOf(sop) + it }
        return sop.id
    }

    override fun updateSopTitle(sopId: String, title: String) {
        val resolvedTitle = title.ifBlank { "Ny SOP" }
        val now = LocalDateTime.now()
        sopState.update { current ->
            current.map { sop ->
                if (sop.id == sopId) {
                    sop.copy(title = resolvedTitle, updatedAt = now)
                } else {
                    sop
                }
            }
        }
    }

    override fun upsertStep(
        sopId: String,
        stepId: String?,
        title: String,
        description: String,
        imagePaths: List<String>,
    ) {
        val now = LocalDateTime.now()
        sopState.update { current ->
            current.map { sop ->
                if (sop.id != sopId) {
                    sop
                } else {
                    val existingStep = sop.steps.firstOrNull { it.id == stepId }
                    val resolvedStepId = existingStep?.id ?: UUID.randomUUID().toString()
                    val resolvedImages = imagePaths.map { path ->
                        existingStep?.images?.firstOrNull { it.filePath == path } ?: StepImage(
                            id = UUID.randomUUID().toString(),
                            stepId = resolvedStepId,
                            filePath = path,
                            createdAt = now,
                        )
                    }
                    val resolvedStep = Step(
                        id = resolvedStepId,
                        sopId = sopId,
                        stepNumber = existingStep?.stepNumber ?: sop.steps.size + 1,
                        title = title.ifBlank { "Nyt trin" },
                        description = description.trim(),
                        images = resolvedImages,
                        createdAt = existingStep?.createdAt ?: now,
                        updatedAt = now,
                    )
                    val updatedSteps = if (existingStep == null) {
                        sop.steps + resolvedStep
                    } else {
                        sop.steps.map { step ->
                            if (step.id == existingStep.id) {
                                resolvedStep
                            } else {
                                step
                            }
                        }
                    }
                    sop.copy(steps = updatedSteps.sortedBy { it.stepNumber }, updatedAt = now)
                }
            }
        }
    }
}
