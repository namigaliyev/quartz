package com.scheduler.quartz.app.job

import com.scheduler.quartz.adapter.mysql.QuartzJobRepository
import com.scheduler.quartz.app.kafka.JobProducer
import com.scheduler.quartz.app.model.JobData
import lombok.RequiredArgsConstructor
import org.quartz.Job
import org.quartz.JobExecutionContext
import org.slf4j.LoggerFactory
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException
import org.springframework.stereotype.Service
import java.lang.RuntimeException

@Service
@RequiredArgsConstructor
class EventProducerJob(
    val repository: QuartzJobRepository,
    val jobProducer: JobProducer
): Job {

    override fun execute(context: JobExecutionContext?) {
        val jobKey = context?.jobDetail?.key
        log.info("key=${jobKey?.name}")

        val jobDetail = repository.findQuartzJobByJobKey(jobKey?.name!!)
            .orElseThrow {NotFoundException()}

        log.info("jobDetail found for jobKey=${jobKey.name} operationId=${jobDetail.operationId}")

        val jobData = JobData(
            product = jobDetail.product!!,
            operationId = jobDetail.operationId!!,
            cronExpression = jobDetail.cronExpression!!,
            kafkaTopic = jobDetail.kafkaTopic!!,
            description = jobDetail.description,
            data = context.jobDetail?.jobDataMap?.get("data")
        )

        log.info("jobData=$jobData")

        jobProducer.produceJob(
            jobKey = jobKey.name,
            jobData = jobData
        )
    }

    companion object {
        private var log = LoggerFactory.getLogger(this::class.java)
    }

}