package com.scheduler.quartz.app.kafka

import com.scheduler.quartz.app.model.JobData
import org.slf4j.LoggerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component

@Component
class JobProducer(
    private val kafkaTemplate: KafkaTemplate<String, Any>
) {

    fun produceJob(jobKey: String, jobData: JobData<*>) {
        kafkaTemplate.send(jobData.kafkaTopic!!, jobData)
        logger.info("jobKey=[$jobKey] produced")
    }

    companion object {
        private var logger = LoggerFactory.getLogger(this::class.java)
    }
}