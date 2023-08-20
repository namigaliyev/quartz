package com.scheduler.quartz.app

import com.scheduler.quartz.adapter.domain.QuartzJob
import com.scheduler.quartz.adapter.handler.Handler
import com.scheduler.quartz.adapter.mysql.QuartzJobRepository
import com.scheduler.quartz.app.job.EventProducerJob
import com.scheduler.quartz.app.model.JobData
import com.scheduler.quartz.app.util.JobUtil.Companion.jobKeyGen
import com.scheduler.quartz.app.util.SchedulerUtil
import lombok.RequiredArgsConstructor
import lombok.extern.slf4j.Slf4j
import org.quartz.*
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import kotlin.RuntimeException

@Slf4j
@Service
@RequiredArgsConstructor
class CreateJob(
    private val scheduler: SchedulerUtil,
    private val repository: QuartzJobRepository
): Handler<JobData<*>, Void?> {

    override fun handle(request: JobData<*>): Void? {
            val jobKey = jobKeyGen(
                product = request.product!!,
                operationId = request.operationId!!
            )

            repository.findQuartzJobByJobKey(jobKey)
                .ifPresent { throw RuntimeException() }

            scheduler.schedule(
                jobKey = jobKey,
                jobData = request
            )

            log.info("job with key=[$jobKey] scheduled")

            repository.save(
                QuartzJob(
                    jobKey = jobKey,
                    product = request.product,
                    operationId = request.operationId,
                    jobClassName = jobClassName,
                    cronExpression = request.cronExpression,
                    kafkaTopic = request.kafkaTopic,
                    description = request.description
                )
            )

            log.info("job with key=[$jobKey] saved")

        return null;
    }

    companion object {
        private var log = LoggerFactory.getLogger(this::class.java)
        val jobClassName: String = EventProducerJob::class.java.name
    }
}