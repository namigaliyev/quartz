package com.scheduler.quartz.app.model

data class JobData<T> (
    val product: String,
    val operationId: String,
    val cronExpression: String,
    val kafkaTopic: String,
    val description: String? = null,
    val data: T
)