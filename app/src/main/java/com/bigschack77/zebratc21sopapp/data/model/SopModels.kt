package com.bigschack77.zebratc21sopapp.data.model

import java.time.LocalDateTime

data class Sop(
    val id: String,
    val title: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val steps: List<Step> = emptyList(),
)

data class Step(
    val id: String,
    val sopId: String,
    val stepNumber: Int,
    val title: String,
    val description: String,
    val images: List<StepImage> = emptyList(),
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
)

data class StepImage(
    val id: String,
    val stepId: String,
    val filePath: String,
    val createdAt: LocalDateTime,
)
